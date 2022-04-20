package io.github.sdev.application.ports.out

import io.github.sdev.scraper.News

trait ScraperService[F[_]] {
  def scrapNews(siteUrl: String): F[List[News]]
}
