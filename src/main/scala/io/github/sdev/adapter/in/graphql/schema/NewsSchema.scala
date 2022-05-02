package io.github.sdev.adapter.in.graphql.schema

import io.github.sdev.application.GetNewsUseCaseService
import io.github.sdev.domain.entities.News
import sangria.schema._

object NewsSchema {

  def apply[F[_]]: ObjectType[GetNewsUseCaseService[F], News] =
    ObjectType(
      name = "News",
      fieldsFn = () =>
        fields(
          Field(
            name = "title",
            fieldType = StringType,
            resolve = _.value.title
          ),
          Field(
            name = "link",
            fieldType = StringType,
            resolve = _.value.link
          )
        )
    )
}
