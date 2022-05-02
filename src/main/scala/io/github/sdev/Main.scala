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
import io.github.sdev.adapter.in.graphql.schema.{NewsDeferredResolver, QueryType}
import io.github.sdev.adapter.in.rest.NewsRoutes
import io.github.sdev.adapter.out.cache.{CacheConfig, CacheServiceImpl}
import io.github.sdev.adapter.out.persistence.NewsRepositoryImpl
import io.github.sdev.application.GetNewsUseCaseService
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
      scraperService = new ScraperServiceImpl[F]
      newsRepository = new NewsRepositoryImpl[F](xa)
      cacheService   = new CacheServiceImpl[F](redisCommands, cacheConfig)
      getNewsUseCaseService = new GetNewsUseCaseService[F](
        scraperService,
        newsRepository,
        cacheService,
        config.scraper.url
      )
      graphQL <- Resource.eval(
        new SangriaGraphQL(
          Schema(query = QueryType[F](dispatcher)),
          new NewsDeferredResolver[F](dispatcher),
          getNewsUseCaseService.pure[F]
        ).pure[F]
      )
      httpApp = (
        NewsRoutes.endpoints[F](getNewsUseCaseService) <+> GraphQLRoutes[F](graphQL)
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
