package io.github.sdev.scraper.playground

import cats.effect.IOApp
import io.github.sdev.scraper.Scraper._
import cats.effect.ExitCode

object Example extends IOApp.Simple {

  val run =
    scrapNews("https://www.nytimes.com/")
      .map(news => news.foreach(println))
      .map(_ => ExitCode.Success)
}
