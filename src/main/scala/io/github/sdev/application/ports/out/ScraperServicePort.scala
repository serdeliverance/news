package io.github.sdev.application.ports.out

import io.github.sdev.domain.entities.News

trait ScraperServicePort[F[_]] {
  def scrapNews(siteUrl: String): F[List[News]]
}
