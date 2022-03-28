package io.github.sdev.scraper

import zio.IO

object Scraper:
  def scrapNews(siteUrl: String): IO[ScrapError, List[News]] = ???
