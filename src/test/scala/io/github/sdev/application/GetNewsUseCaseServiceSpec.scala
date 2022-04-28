package io.github.sdev.application

import munit.CatsEffectSuite
import org.mockito.Mockito.when
import org.mockito.MockitoSugar.mock
import io.github.sdev.application.ports.out.ScraperService
import cats.effect.IO
import io.github.sdev.application.ports.out.NewsRepository
import io.github.sdev.application.ports.out.CacheService
import org.typelevel.log4cats.slf4j.Slf4jLogger
import io.github.sdev.domain.entities.News

class GetNewsUseCaseServiceSpec extends CatsEffectSuite {

  implicit val logger = Slf4jLogger.getLogger[IO]

  private val scraperService = mock[ScraperService[IO]]
  private val newsRepository = mock[NewsRepository[IO]]
  private val cacheService   = mock[CacheService[IO]]
  private val url            = "https://www.nytimes.com"

  private val subject = new GetNewsUseCaseService[IO](scraperService, newsRepository, cacheService, url)

  test("getting news from web site") {
    val result = subject.getNews()

    assertIO(result, List(News("A new", "https://nytimes.com/a-new"), News("Another new", "https://nytimes.com/another-new")))
  }

  test("getting news from cache") {
    val result = subject.getNews()

    assertIO(result, List(News("A new", "https://nytimes.com/a-new"), News("Another new", "https://nytimes.com/another-new")))
  }

  test("falling getting news from website") {
    val result = subject.getNews()

    result.attempt.map {
      case Left(ex)     => assert(ex == ScrapingError)
      case Right(value) => fail("ble")
    }
  }
}
