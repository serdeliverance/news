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
import cats.effect.Resource
import cats.effect.Async

import io.github.sdev.adapter.in.web.NewsRoutes
import io.github.sdev.application.GetNewsUseCaseService
import io.github.sdev.application.ports.out.ScraperService
import io.github.sdev.scraper.ScraperServiceImpl
import io.github.sdev.adapter.out.persistence.NewsRepositoryImpl
import io.github.sdev.application.ports.out.CacheService
import io.github.sdev.adapter.out.cache.CacheServiceImpl
import io.github.sdev.adapter.out.cache.CacheConfig

object Main extends IOApp {

  def createServer[F[_]: Async]: Stream[F, Nothing] = {
    for {
      client      <- Stream.resource(EmberClientBuilder.default[F].build)
      sessions    <- ??? // TODO create skunk session
      redisClient <- ??? // TODO create redis client
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
  }.drain

  def run(args: List[String]) = ???
}
