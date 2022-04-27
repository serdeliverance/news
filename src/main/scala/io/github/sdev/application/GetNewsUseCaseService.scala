package io.github.sdev.application

import io.github.sdev.domain.usecases.GetNewsUseCase
import io.github.sdev.domain.entities.News
import io.github.sdev.application.ports.out.ScraperService
import io.github.sdev.application.ports.out.CacheService
import io.github.sdev.application.ports.out.NewsRepository

import cats.syntax.all._
import cats.Monad
import cats.effect.IO
import cats.Applicative
import org.typelevel.log4cats.Logger

class GetNewsUseCaseService[F[_]: Monad: Applicative: Logger](
    scraperService: ScraperService[F],
    newsRepository: NewsRepository[F],
    cache: CacheService[F]
) extends GetNewsUseCase[F] {

  // TODO remove hardcoding and extract into configuration
  private val URL = "https://www.nytimes.com/"

  override def getNews(): F[List[News]] =
    for {
      _          <- Logger[F].info("Retrieving news")
      cachedNews <- cache.getAll()
      news <-
        if (cachedNews.nonEmpty)
          cachedNews.pure[F]
        else
          Logger[F].info("Scraping news from web") *>
            scraperService
              .scrapNews(URL)
              .flatTap { news =>
                Logger[F].info("Saving news into db and updating cache") *> newsRepository.save(news) *> cache.save(news)
              }
    } yield news
}
