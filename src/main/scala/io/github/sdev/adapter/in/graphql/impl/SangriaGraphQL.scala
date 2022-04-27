package io.github.sdev.adapter.in.graphql.impl

import io.github.sdev.adapter.in.graphql.GraphQL
import io.circe.Json

object SangriaGraphQL {
  // TODO implement
  def apply[F[_]] = new GraphQL[F] {
    // TODO implement
    override def query(request: Json): F[Either[Json, Json]] = ???
  }
}
