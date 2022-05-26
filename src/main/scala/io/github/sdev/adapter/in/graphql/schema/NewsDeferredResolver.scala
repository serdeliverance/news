package io.github.sdev.adapter.in.graphql.schema

import scala.concurrent.{ ExecutionContext, Future }

import cats.effect.std.Dispatcher
import io.github.sdev.application.ports.in.GetNewsUseCase
import sangria.execution.deferred.{ Deferred, DeferredResolver }

class NewsDeferredResolver[F[_]](dispatcher: Dispatcher[F]) extends DeferredResolver[GetNewsUseCase[F]] {

  override def resolve(deferred: Vector[Deferred[Any]], ctx: GetNewsUseCase[F], queryState: Any)(implicit
      ec: ExecutionContext
  ): Vector[Future[Any]] =
    Vector(dispatcher.unsafeToFuture(ctx.getNews()))
}
