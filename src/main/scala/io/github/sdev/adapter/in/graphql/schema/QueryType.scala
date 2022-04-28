package io.github.sdev.adapter.in.graphql.schema

import sangria.schema.ObjectType
import io.github.sdev.application.GetNewsUseCaseService

object QueryType {

  // TODO implement
  def apply[F[_]](): ObjectType[GetNewsUseCaseService[F], Unit] = ???
}
