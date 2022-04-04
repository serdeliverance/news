package io.github.sdev.application

import io.github.sdev.domain.usecases.GetNewsUseCase
import cats.effect.IO
import io.github.sdev.scraper.News
import io.github.sdev.application.ports.out.ScraperService
import io.github.sdev.application.ports.out.CacheService
import io.github.sdev.application.ports.out.NewsRepository

class GetNewsUseCaseService(scraperService: ScraperService, newsRepository: NewsRepository, cache: CacheService) extends GetNewsUseCase {
  override def getNews(): IO[List[News]] =
    for {
      cachedNews <- cache.getAll()
      isCacheEmpty = cachedNews.isEmpty
      news <-
        if (isCacheEmpty)
          newsRepository.findAll()
        else IO.pure(cachedNews)
      _ <- updateCacheConditionally(isCacheEmpty, news)
      areNewHeadlinesAvailable = isCacheEmpty
      _ <- saveNewsConditionally(areNewHeadlinesAvailable, news)
    } yield news

  // TODO
  private def updateCacheConditionally(condition: Boolean, news: => List[News]): IO[Unit] = ???

  // TODO
  private def saveNewsConditionally(condition: Boolean, news: => List[News]): IO[Unit] = ???
}
