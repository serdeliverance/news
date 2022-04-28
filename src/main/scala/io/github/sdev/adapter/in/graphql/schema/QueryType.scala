package io.github.sdev.adapter.in.graphql.schema

import sangria.schema._
import io.github.sdev.application.GetNewsUseCaseService
import cats.effect.Async
import cats.effect.std.Dispatcher

object QueryType {

  def apply[F[_]: Async](dispatcher: Dispatcher[F]): ObjectType[GetNewsUseCaseService[F], Unit] =
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
