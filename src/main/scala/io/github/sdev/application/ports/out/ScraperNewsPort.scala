package io.github.sdev.application.ports.out

import io.github.sdev.domain.entities.News

trait ScraperNewsPort[F[_]] {
  def scrapNews(siteUrl: String): F[List[News]]
}
