package io.github.sdev.adapter.in.rest

import cats.effect.Sync
import io.github.sdev.application.ports.in.GetNewsUseCase
import io.github.sdev.application.json.SerDes.newsEncoder
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.dsl.Http4sDsl

object NewsRoutes {

  def endpoints[F[_]: Sync](getNewsUseCaseService: GetNewsUseCase[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] { case GET -> Root / "news" =>
      Ok(getNewsUseCaseService.getNews())
    }
  }
}
