package io.github.sdev.adapter.in.graphql.schema

import cats.effect.Async
import cats.effect.std.Dispatcher
import io.github.sdev.application.ports.in.GetNewsUseCase
import sangria.schema._

object QueryType {

  def apply[F[_]: Async](dispatcher: Dispatcher[F]): ObjectType[GetNewsUseCase[F], Unit] =
    ObjectType(
      name = "Query",
      fields = fields(
        Field(
          name = "news",
          fieldType = ListType(NewsSchema[F]),
          description = Some("Returns news from NyTimes web site."),
          resolve = c => dispatcher.unsafeToFuture(c.ctx.getNews())
        )
      )
    )
}
