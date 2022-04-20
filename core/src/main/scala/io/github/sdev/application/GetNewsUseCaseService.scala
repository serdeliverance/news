package io.github.sdev.application

import io.github.sdev.domain.usecases.GetNewsUseCase
import cats.effect.IO
import io.github.sdev.scraper.News
import io.github.sdev.application.ports.out.ScraperService
import io.github.sdev.application.ports.out.CacheService
import io.github.sdev.application.ports.out.NewsRepository

// TODO add tagless final to this algebra (we are hardcoding IO here)
class GetNewsUseCaseService(scraperService: ScraperService[IO], newsRepository: NewsRepository, cache: CacheService)
    extends GetNewsUseCase {
  override def getNews(): IO[List[News]] =
    for {
      cachedNews <- cache.getAll()
      isCacheEmpty = cachedNews.isEmpty
      news <-
        if (isCacheEmpty)
          newsRepository.findAll()
        else IO.pure(cachedNews)
      _ <- IO.whenA(isCacheEmpty)(cache.save(news))
      areNewHeadlinesAvailable = isCacheEmpty
      _ <- IO.whenA(isCacheEmpty)(newsRepository.save(news))
    } yield news
}
