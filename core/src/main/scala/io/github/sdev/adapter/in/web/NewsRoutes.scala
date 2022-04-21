package io.github.sdev.adapter.in.web

import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.Sync

object NewsRoutes {
  def routes[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] { case GET -> Root / "news" =>
      Ok("Ok")
    }
  }
}
