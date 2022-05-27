package io.github.sdev.adapter.in.graphql.impl

import io.circe.Json
import io.circe.optics.JsonPath.root
import sangria.execution.WithViolations
import sangria.parser.SyntaxError
import sangria.validation.AstNodeViolation

object Util {

  object Formatter {

    def formatString(string: String): Json = Json.obj(
      "errors" -> Json.arr(Json.obj("message" -> Json.fromString(string)))
    )

    def formatSyntaxError(e: SyntaxError): Json = Json.obj(
      "errors" -> Json.arr(
        Json.obj(
          "message" -> Json.fromString(e.getMessage),
          "locations" -> Json.arr(
            Json.obj("line" -> Json.fromInt(e.originalError.position.line), "column" -> Json.fromInt(e.originalError.position.column))
          )
        )
      )
    )

    def formatThrowable(e: Throwable): Json =
      Json.obj("errors" -> Json.arr(Json.obj("class" -> Json.fromString(e.getClass.getName), "message" -> Json.fromString(e.getMessage))))

    def formatWithViolations(e: WithViolations): Json = Json.obj("errors" -> Json.fromValues(e.violations.map {
      case v: AstNodeViolation =>
        Json.obj(
          "message" -> Json.fromString(v.errorMessage),
          "locations" -> Json.fromValues(
            v.locations.map(loc => Json.obj("line" -> Json.fromInt(loc.line), "column" -> Json.fromInt(loc.column)))
          )
        )
      case v => Json.obj("message" -> Json.fromString(v.errorMessage))
    }))
  }

  object JsonParsing {
    val queryStringLens   = root.query.string
    val operationNameLens = root.operationName.string
    val variablesLens     = root.variables.obj
  }
}
