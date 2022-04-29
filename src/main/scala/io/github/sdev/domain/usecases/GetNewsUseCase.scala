package io.github.sdev.domain.usecases

import io.github.sdev.domain.entities.News

trait GetNewsUseCase[F[_]] {
  def getNews(): F[List[News]]
}
