package io.github.sdev.application.ports.out

import io.github.sdev.scraper.News

trait NewsRepository[F[_]] {

  def findAll(): F[List[News]]

  def save(news: News): F[Unit]

  def save(news: List[News]): F[Unit]
}
