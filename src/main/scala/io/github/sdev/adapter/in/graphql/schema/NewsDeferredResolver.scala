package io.github.sdev.adapter.in.graphql.schema

import cats.effect.implicits._
import cats.effect._
import cats.implicits._
import sangria.execution.deferred.DeferredResolver
import io.github.sdev.application.GetNewsUseCaseService
import sangria.execution.deferred.Deferred
import scala.concurrent.{ ExecutionContext, Future }
import cats.effect.std.Dispatcher

class NewsDeferredResolver[F[_]](dispatcher: Dispatcher[F]) extends DeferredResolver[GetNewsUseCaseService[F]] {

  override def resolve(deferred: Vector[Deferred[Any]], ctx: GetNewsUseCaseService[F], queryState: Any)(implicit
      ec: ExecutionContext
  ): Vector[Future[Any]] =
    Vector(dispatcher.unsafeToFuture(ctx.getNews()))
}
