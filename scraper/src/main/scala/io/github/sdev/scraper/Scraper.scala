package io.github.sdev.scraper

class Scraper private (site: String):
  def scrap(tag: Tag): IO[ScrapError, List[News]] = ???

object Scraper:
  def make(site: String) = new Scraper(site)
