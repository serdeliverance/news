package io.github.sdev.application.ports.out

import io.github.sdev.domain.entities.News

trait NewsRepositoryPort[F[_]] {

  def save(news: News): F[Unit]

  def save(news: List[News]): F[Unit]
}
