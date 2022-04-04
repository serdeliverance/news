package io.github.sdev.adapter.out.cache

import io.github.sdev.application.ports.out.CacheService
import cats.effect.IO
import io.github.sdev.scraper.News

class CacheServiceImpl extends CacheService {
  // TODO
  override def getAll(): IO[List[News]] = ???
}
