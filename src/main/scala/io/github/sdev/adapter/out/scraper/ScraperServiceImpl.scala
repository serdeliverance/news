package io.github.sdev.scraper

import cats.effect.Sync
import cats.syntax.all._
import io.github.sdev.application.ports.out.ScraperService
import io.github.sdev.domain.entities.News
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import org.typelevel.log4cats.Logger

class ScraperServiceImpl[F[_]: Sync: Logger] extends ScraperService[F] {
  // TODO add error handling
  def scrapNews(siteUrl: String): F[List[News]] = {
    val browser = JsoupBrowser()
    for {
      _   <- Logger[F].info(s"Scraping news from $siteUrl")
      doc <- browser.get(siteUrl).pure[F]
      news <- Sync[F]
        .delay(doc >> elementList("#site-content section [class=story-wrapper]"))
        .map { result =>
          result.map(parseContent).collect { case Right(news) if news.title.nonEmpty => news }
        }
    } yield news
  }

  private def parseContent(element: Element): Either[ScrapError, News] = {
    val title     = element >> allText("h3")
    val maybeLink = (element >> "a" >> attrs("href")).headOption
    maybeLink match {
      case Some(link) => Right(News(title, link))
      case None       => Left(ScrapError(element, "doesn't have link"))
    }
  }
}
