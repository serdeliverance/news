package io.github.sdev.adapter.in.web

import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import cats.effect.Sync
import io.github.sdev.application.GetNewsUseCaseService
import org.http4s.circe.CirceEntityDecoder._

object NewsRoutes {

  def endpoints[F[_]: Sync](getNewsUseCaseService: GetNewsUseCaseService[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] { case GET -> Root / "news" =>
      getNewsUseCaseService.getNews().flatMap(Ok(_))
    }
  }
}
