package io.github.sdev.adapter.in.web

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
import cats.effect.IO

object HttpServer {
  def make[F[_]: Async]: Stream[F, Nothing] = {
    for {
      client <- Stream.resource(EmberClientBuilder.default[F].build)
      httpApp = (
        NewsRoutes.routes[F]
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
}
