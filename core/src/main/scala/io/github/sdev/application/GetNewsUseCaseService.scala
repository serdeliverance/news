package io.github.sdev.application

import io.github.sdev.domain.usecases.GetNewsUseCase
import io.github.sdev.scraper.News
import io.github.sdev.application.ports.out.ScraperService
import io.github.sdev.application.ports.out.CacheService
import io.github.sdev.application.ports.out.NewsRepository

import cats.syntax.all._
import cats.Monad
import cats.effect.IO
import cats.Applicative

class GetNewsUseCaseService[F[_]: Monad: Applicative](
    scraperService: ScraperService[F],
    newsRepository: NewsRepository[F],
    cache: CacheService[F]
) extends GetNewsUseCase[F] {

  // TODO refactor flow using Effects system niceties
  override def getNews(): F[List[News]] =
    for {
      cachedNews <- cache.getAll()
      isCacheEmpty = cachedNews.isEmpty
      news <-
        if (isCacheEmpty)
          newsRepository.findAll()
        else cachedNews.pure[F]
      _ <- Applicative[F].whenA(isCacheEmpty)(cache.save(news))
      areNewHeadlinesAvailable = isCacheEmpty
      _ <- Applicative[F].whenA(isCacheEmpty)(newsRepository.save(news))
    } yield news
}
