package io.github.sdev.adapter.out.cache

import io.github.sdev.application.ports.out.CacheService
import cats.effect.IO
import io.github.sdev.scraper.News
import cats.effect.kernel.Resource
import dev.profunktor.redis4cats.algebra.StringCommands

class CacheServiceImpl(redisClient: Resource[IO, StringCommands[IO, String, String]], config: CacheConfig) extends CacheService {
  // TODO
  override def getAll(): IO[List[News]] = ???

  // TODO
  override def save(news: List[News]): IO[Unit] =
    redisClient.use { command =>
      command.set("news", news, config.ttl) // FIXME
    }
}
