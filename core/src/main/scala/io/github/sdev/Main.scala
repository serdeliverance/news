package example.io.github.sdev

import cats.effect.IOApp
import cats.effect.ExitCode

import org.http4s.ember.client.EmberClientBuilder
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger
import cats.syntax.all._
import com.comcast.ip4s._

import io.github.sdev.adapter.in.web.NewsRoutes
import io.github.sdev.application.GetNewsUseCaseService
import io.github.sdev.application.ports.out.ScraperService
import io.github.sdev.scraper.ScraperServiceImpl
import io.github.sdev.adapter.out.persistence.NewsRepositoryImpl
import io.github.sdev.application.ports.out.CacheService
import io.github.sdev.adapter.out.cache.CacheServiceImpl
import io.github.sdev.adapter.out.cache.CacheConfig
import cats.effect.{ Async, Resource }
import cats.effect.std.Console
import skunk._
import skunk.implicits._
import skunk.codec.all._
import natchez.Trace.Implicits.noop
import cats.effect.std
import dev.profunktor.redis4cats.connection.RedisClient
import dev.profunktor.redis4cats.Redis
import dev.profunktor.redis4cats
import dev.profunktor.redis4cats.data.RedisCodec
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.Logger
import dev.profunktor.redis4cats.effect.Log

object Main extends IOApp {

  def createServer[F[_]: Async: Console: Log] = {
    val stringCodec: redis4cats.data.RedisCodec[String, String] =
      RedisCodec.Utf8

    implicit val logger: Logger[F] =
      Slf4jLogger.getLogger[F]

    for {
      sessions <- Session
        .single[F](
          host = "localhost",
          port = 5432,
          user = "root",
          database = "news",
          password = Some("root")
        )
        .pure[F]
      redisClient <- RedisClient[F]
        .from("redis://localhost")
        .flatMap(Redis[F].fromClient(_, stringCodec))
        .pure[F]
      cacheConfig           = CacheConfig(1000 * 3600)  // TODO remove hardcoding and extract from configuration
      scraperService        = new ScraperServiceImpl[F] // TODO add logger (it conflicts with line 35)
      newsRepository        = new NewsRepositoryImpl[F](sessions)
      cacheService          = new CacheServiceImpl[F](redisClient, cacheConfig)
      getNewsUseCaseService = new GetNewsUseCaseService[F](scraperService, newsRepository, cacheService)
      httpApp = (
        NewsRoutes.endpoints[F](getNewsUseCaseService)
      ).orNotFound
      finalHttpApp = Logger.httpApp(true, true)(httpApp)
      exitCode <- Stream.resource(
        EmberServerBuilder
          .default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build >>
          Resource.eval(Async[F].never)
      )
    } yield exitCode
  }

  def run(args: List[String]) = ???
}
