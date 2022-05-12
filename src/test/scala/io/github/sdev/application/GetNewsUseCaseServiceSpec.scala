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
import io.github.sdev.stubs.NewsStubs
import javax.naming.ServiceUnavailableException

class GetNewsUseCaseServiceSpec extends CatsEffectSuite with NewsStubs {

  implicit val logger = Slf4jLogger.getLogger[IO]

  private val scraperService = mock[ScraperService[IO]]
  private val newsRepository = mock[NewsRepository[IO]]
  private val cacheService   = mock[CacheService[IO]]
  private val url            = "https://www.nytimes.com"

  private val subject = new GetNewsUseCaseService[IO](scraperService, newsRepository, cacheService, url)

  test("getting news from web site") {
    when(cacheService.getAll()).thenReturn(IO.pure(List.empty))
    when(scraperService.scrapNews(url)).thenReturn(IO.pure(aListOfNews))
    when(newsRepository.save(aListOfNews)).thenReturn(IO.unit)
    when(cacheService.save(aListOfNews)).thenReturn(IO.unit)

    val result = subject.getNews()

    assertIO(result, List(News("A new", "https://nytimes.com/a-new"), News("Another new", "https://nytimes.com/another-new")))
  }

  test("getting news from cache") {
    when(cacheService.getAll()).thenReturn(IO.pure(aListOfNews))

    val result = subject.getNews()

    assertIO(result, List(News("A new", "https://nytimes.com/a-new"), News("Another new", "https://nytimes.com/another-new")))
  }

  test("falling getting news from website") {
    when(cacheService.getAll()).thenReturn(IO.pure(List.empty))
    when(scraperService.scrapNews(url)).thenReturn(IO.raiseError(new RuntimeException))

    val result = subject.getNews()

    result.attempt.map {
      case Left(ex)     => assert(ex.isInstanceOf[RuntimeException])
      case Right(value) => fail("assertion fail")
    }
  }
}
