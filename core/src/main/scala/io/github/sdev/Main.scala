package example.io.github.sdev

import cats.effect.IOApp
import cats.effect.ExitCode

import org.http4s.ember.client.EmberClientBuilder
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.{ Logger => HttpLogger }
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
import cats.effect.IO
import dev.profunktor.redis4cats.effect.Log.Stdout._

object Main extends IOApp {

  def createServer[F[_]: Async: Console] = {
    val stringCodec: redis4cats.data.RedisCodec[String, String] =
      RedisCodec.Utf8

    implicit val logger: Logger[F] =
      Slf4jLogger.getLogger[F]

    for {
      sessions <- Session
        .pooled[F](
          host = "localhost",
          port = 5432,
          user = "root",
          database = "news",
          password = Some("root"),
          max = 10
        )
      redisCommands <- Redis[F].utf8("redis://localhost")
      cacheConfig           = CacheConfig(1000 * 3600) // TODO remove hardcoding and extract from configuration
      scraperService        = new ScraperServiceImpl[F]
      newsRepository        = new NewsRepositoryImpl[F](sessions)
      cacheService          = new CacheServiceImpl[F](redisCommands, cacheConfig)
      getNewsUseCaseService = new GetNewsUseCaseService[F](scraperService, newsRepository, cacheService)
      httpApp = (
        NewsRoutes.endpoints[F](getNewsUseCaseService)
      ).orNotFound
      finalHttpApp = HttpLogger.httpApp(true, true)(httpApp)
      server <-
        EmberServerBuilder
          .default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build
    } yield server
  }

  def run(args: List[String]) = createServer[IO].use(_ => IO.never).as(ExitCode.Success)
}
