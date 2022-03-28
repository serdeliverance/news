package io.github.sdev.adapter.web

import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.IO

object NewsRoutes:
  def routes: HttpRoutes[IO] =
    val dsl = new Http4sDsl[IO] {}
    import dsl.*
    HttpRoutes.of[IO] { case GET -> Root / "news" =>
      Ok("Ok")
    }
