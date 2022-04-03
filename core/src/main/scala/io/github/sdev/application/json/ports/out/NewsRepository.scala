package io.github.sdev.application.json.ports.out

import cats.effect.IO
import io.github.sdev.scraper.News

trait NewsRepository {

  def findAll(): IO[List[News]]
}
