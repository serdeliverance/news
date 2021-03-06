package io.github.sdev.application.ports.out

import io.github.sdev.domain.entities.News

trait CacheNewsPort[F[_]] {
  def getAll(): F[List[News]]

  def save(news: List[News]): F[Unit]
}
