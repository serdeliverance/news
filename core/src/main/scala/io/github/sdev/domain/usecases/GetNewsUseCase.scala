package io.github.sdev.domain.usecases

import cats.effect.IO
import io.github.sdev.scraper.News

trait GetNewsUseCase[F[_]] {
  def getNews(): F[List[News]]
}
