package io.github.sdev.application.ports.out

import cats.effect.IO
import io.github.sdev.scraper.News

trait CacheService[F[_]] {
  def getAll(): F[List[News]]

  def save(news: List[News]): F[Unit]
}
