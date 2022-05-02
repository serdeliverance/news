package io.github.sdev.application

import cats.syntax.all._
import cats.{ Applicative, Monad }
import io.github.sdev.application.ports.out.{ CacheService, NewsRepository, ScraperService }
import io.github.sdev.domain.entities.News
import io.github.sdev.domain.usecases.GetNewsUseCase
import org.typelevel.log4cats.Logger

class GetNewsUseCaseService[F[_]: Monad: Applicative: Logger](
    scraperService: ScraperService[F],
    newsRepository: NewsRepository[F],
    cache: CacheService[F],
    url: String
) extends GetNewsUseCase[F] {
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
              .scrapNews(url)
              .flatTap { news =>
                Logger[F].info("Saving news into db and updating cache") *> newsRepository.save(
                  news
                ) *> cache.save(news)
              }
    } yield news
}
