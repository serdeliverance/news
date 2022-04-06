package io.github.sdev.application.ports.out

import cats.effect.IO
import io.github.sdev.scraper.News

trait CacheService {
  def getAll(): IO[List[News]]

  def save(news: List[News]): IO[Unit]
}
