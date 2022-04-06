package io.github.sdev.application.ports.out

import cats.effect.IO
import io.github.sdev.scraper.News

trait ScraperService {
  def scrapNews(siteUrl: String): IO[List[News]]
}
