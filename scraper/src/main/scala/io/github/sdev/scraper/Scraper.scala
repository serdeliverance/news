package io.github.sdev.scraper

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.Element
import cats.effect.IO

object Scraper{
  // TODO improve IO using (maybe delay, etc it depends on the case)
  def scrapNews(siteUrl: String): IO[List[News]] =
    for {
      browser <- IO(JsoupBrowser())
      doc     <- IO(browser.get(siteUrl))
      stories <- IO(doc >> elementList("#site-content section [class=story-wrapper]"))
      news = stories.map(parseContent).collect { case Right(news) => news }
    } yield news

  private def parseContent(element: Element): Either[ScrapError, News] ={
    val title     = element >> allText("h3")
    val maybeLink = (element >> "a" >> attrs("href")).headOption
    maybeLink match{
      case Some(link) => Right(News(title, link))
      case None       => Left(ScrapError(element, "doesn't have link"))}}
}