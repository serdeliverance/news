package io.github.sdev.application.json

import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._
import io.circe.{ Decoder, Encoder, Printer }
import io.github.sdev.domain.entities.News

object SerDes {
  implicit val customPrinter: Printer      = Printer.noSpaces.copy(dropNullValues = true)
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val newsEncoder: Encoder[News] = deriveConfiguredEncoder[News]
  implicit val newsDecoder: Decoder[News] = deriveConfiguredDecoder[News]
}
