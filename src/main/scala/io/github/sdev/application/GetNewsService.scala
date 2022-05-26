package io.github.sdev.application

import cats.syntax.all._
import cats.{ Applicative, Monad }
import io.github.sdev.application.ports.out.{ CacheServicePort, NewsRepositoryPort, ScraperServicePort }
import io.github.sdev.domain.entities.News
import org.typelevel.log4cats.Logger
import io.github.sdev.application.ports.in.GetNewsUseCase

object GetNewsService {

  def make[F[_]: Monad: Applicative: Logger](
      scraperService: ScraperServicePort[F],
      newsRepository: NewsRepositoryPort[F],
      cache: CacheServicePort[F],
      url: String
  ): GetNewsUseCase[F] =
    new GetNewsUseCase[F] {
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
}
