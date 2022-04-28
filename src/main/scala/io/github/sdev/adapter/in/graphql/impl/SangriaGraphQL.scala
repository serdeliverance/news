package io.github.sdev.adapter.in.graphql.impl

import io.github.sdev.adapter.in.graphql.GraphQL
import io.circe.Json
import io.circe.JsonObject
import io.circe.optics.JsonPath.root
import cats.syntax.all._
import cats.Applicative
import sangria.parser.QueryParser
import scala.util.Failure
import scala.util.Success
import sangria.parser.SyntaxError
import sangria.execution.deferred.DeferredResolver
import scala.concurrent.ExecutionContext
import sangria.schema.Schema
import sangria.ast.Document
import sangria.execution.WithViolations
import sangria.validation.AstNodeViolation
import Util.Formatter._
import Util.JsonParsing._
import sangria.execution.HandledException
import _root_.sangria.execution._
import _root_.sangria.marshalling.circe._
import cats.effect.Async

class SangriaGraphQL[F[_]: Async: Applicative, A](
    schema: Schema[A, Unit],
    deferredResolver: DeferredResolver[A],
    userContext: F[A]
) extends GraphQL[F] {

  override def query(request: Json): F[Either[Json, Json]] = {
    val queryString   = queryStringLens.getOption(request)
    val operationName = operationNameLens.getOption(request)
    val variables     = variablesLens.getOption(request).getOrElse(JsonObject())
    queryString match {
      case Some(qs) => query(qs, operationName, variables)
      case None     => fail(formatString("No 'query' property was present in request"))
    }
  }

  override def query(query: String, operationName: Option[String], variables: JsonObject): F[Either[Json, Json]] =
    QueryParser.parse(query) match {
      case Success(ast)                       => exec(schema, ast, operationName, variables)
      case Failure(e @ SyntaxError(_, _, pe)) => fail(formatSyntaxError(e))
      case Failure(e)                         => fail(formatThrowable(e))
    }

  private def fail(json: Json): F[Either[Json, Json]] =
    json.asLeft.pure[F]

  private def exec(schema: Schema[A, Unit], query: Document, operationName: Option[String], variables: JsonObject): F[Either[Json, Json]] =
    userContext
      .flatMap { ctx =>
        Async[F].executionContext.flatMap { implicit ec =>
          Async[F].async_ { (cb: Either[Throwable, Json] => Unit) =>
            Executor
              .execute(
                schema = schema,
                deferredResolver = deferredResolver,
                queryAst = query,
                userContext = ctx,
                variables = Json.fromJsonObject(variables),
                operationName = operationName,
                exceptionHandler = ExceptionHandler { case (_, e) =>
                  HandledException(e.getMessage)
                }
              )
              .onComplete {
                case Success(value) => cb(Right(value))
                case Failure(error) => cb(Left(error))
              }
          }
        }
      }
      .attempt
      .flatMap {
        case Right(json)               => json.asRight.pure[F]
        case Left(err: WithViolations) => fail(formatWithViolations(err))
        case Left(err)                 => fail(formatThrowable(err))
      }
}
