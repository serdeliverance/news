package example.io.github.sdev

import cats.effect._
import cats.effect.std.{ Console, Dispatcher }
import cats.syntax.all._
import com.comcast.ip4s._
import dev.profunktor.redis4cats
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats.data.RedisCodec
import dev.profunktor.redis4cats.effect.Log.Stdout._
import doobie.hikari._
import doobie.util.ExecutionContexts
import io.github.sdev.adapter.in.graphql.GraphQLRoutes
import io.github.sdev.adapter.in.graphql.impl.SangriaGraphQL
import io.github.sdev.adapter.in.graphql.schema.{ NewsDeferredResolver, QueryType }
import io.github.sdev.adapter.in.rest.NewsRoutes
import io.github.sdev.adapter.out.cache.{ CacheConfig, CacheServiceImpl }
import io.github.sdev.adapter.out.persistence.NewsRepositoryImpl
import io.github.sdev.application.GetNewsService
import io.github.sdev.application.config.Config
import io.github.sdev.scraper.ScraperServiceImpl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.{ Logger => HttpLogger }
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import pureconfig.module.catseffect.syntax._
import sangria.schema.Schema

object Main extends IOApp {

  def createServer[F[_]: Async: Console] = {
    val stringCodec: redis4cats.data.RedisCodec[String, String] =
      RedisCodec.Utf8

    implicit val logger: Logger[F] =
      Slf4jLogger.getLogger[F]

    for {
      config <- Resource.eval(ConfigSource.default.loadF[F, Config.AppConfig]())
      connEc <- ExecutionContexts.fixedThreadPool[F](32)
      xa <-
        HikariTransactor.newHikariTransactor(
          config.db.driver,
          config.db.url,
          config.db.user,
          config.db.password,
          connEc
        )
      redisCommands <- Redis[F].utf8(config.redis.url)
      dispatcher    <- Dispatcher[F]
      cacheConfig    = CacheConfig(config.cache.ttl)
      scraperService = ScraperServiceImpl.make[F]
      newsRepository = NewsRepositoryImpl.make[F](xa)
      cacheService   = CacheServiceImpl.make[F](redisCommands, cacheConfig)
      getNewsUseCase = GetNewsService.make[F](
        scraperService,
        newsRepository,
        cacheService,
        config.scraper.url
      )
      graphQL =
        SangriaGraphQL.make(
          Schema(query = QueryType[F](dispatcher)),
          new NewsDeferredResolver[F](dispatcher),
          getNewsUseCase.pure[F] // FIXME problems with .pure type inference
        )
      httpApp = (
        NewsRoutes.endpoints[F](getNewsUseCase) <+> GraphQLRoutes[F](graphQL)
      ).orNotFound
      finalHttpApp = HttpLogger.httpApp(true, true)(httpApp)
      server <-
        EmberServerBuilder
          .default[F]
          .withHost(ipv4"0.0.0.0") // TODO refactor: remove hardcoded string and use Config instead
          .withPort(port"8080")    // TODO refactor: remove hardcoded string and use Config instead
          .withHttpApp(finalHttpApp)
          .build
    } yield server
  }

  def run(args: List[String]) = createServer[IO].use(_ => IO.never).as(ExitCode.Success)
}
