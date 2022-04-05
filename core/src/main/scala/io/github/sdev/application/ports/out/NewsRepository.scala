package io.github.sdev.application.ports.out

import cats.effect.IO
import io.github.sdev.scraper.News

trait NewsRepository {

  def findAll(): IO[List[News]]

  def save(news: News): IO[Unit]

  def save(news: List[News]): IO[Unit]
}
