package io.github.sdev.application.json

import io.circe.Decoder
import io.circe.Encoder
import io.circe.generic.extras.semiauto._
import io.github.sdev.scraper.News
import io.circe.Printer
import io.circe.generic.extras.Configuration

object SerDes {
  implicit val customPrinter: Printer      = Printer.noSpaces.copy(dropNullValues = true)
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames

  implicit val newsEncoder: Encoder[News] = deriveConfiguredEncoder[News]
  implicit val newsDecoder: Decoder[News] = deriveConfiguredDecoder[News]
}
