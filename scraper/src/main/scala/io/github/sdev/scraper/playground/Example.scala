package io.github.sdev.scraper.playground

import net.ruippeixotog.scalascraper.browser.JsoupBrowser

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.Element
import io.github.sdev.scraper.News
import io.github.sdev.scraper.ScrapError

// TODO delete when library is fully implemented
object Example extends App:
  val browser = JsoupBrowser()
  val doc     = browser.get("https://www.nytimes.com/")

  val stories = doc >> elementList("#site-content section [class=story-wrapper]")

  val news = stories.map(parseContent).collect { case Right(news) => news }

  news.foreach(n => println(format(n.toString)))

  def format(content: String) = s"${"-" * 10}\n$content\n${"-" * 10}"

  def parseContent(element: Element): Either[ScrapError, News] =
    val title     = element >> allText("h3")
    val maybeLink = (element >> "a" >> attrs("href")).headOption
    maybeLink match
      case Some(link) => Right(News(title, link))
      case None       => Left(ScrapError(element, "doesn't have link"))
