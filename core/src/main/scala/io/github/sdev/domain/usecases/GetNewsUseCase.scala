package io.github.sdev.domain.usecases

import cats.effect.IO
import io.github.sdev.scraper.News

trait GetNewsUseCase {
  def getNews(): IO[List[News]]
}
