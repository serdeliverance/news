package io.github.sdev.application.ports.in

import io.github.sdev.domain.entities.News

trait GetNewsUseCase[F[_]] {
  def getNews(): F[List[News]]
}
