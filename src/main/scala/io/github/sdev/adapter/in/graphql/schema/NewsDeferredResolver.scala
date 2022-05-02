package io.github.sdev.adapter.in.graphql.schema

import scala.concurrent.{ ExecutionContext, Future }

import cats.effect.std.Dispatcher
import io.github.sdev.application.GetNewsUseCaseService
import sangria.execution.deferred.{ Deferred, DeferredResolver }

class NewsDeferredResolver[F[_]](dispatcher: Dispatcher[F]) extends DeferredResolver[GetNewsUseCaseService[F]] {

  override def resolve(deferred: Vector[Deferred[Any]], ctx: GetNewsUseCaseService[F], queryState: Any)(implicit
      ec: ExecutionContext
  ): Vector[Future[Any]] =
    Vector(dispatcher.unsafeToFuture(ctx.getNews()))
}
