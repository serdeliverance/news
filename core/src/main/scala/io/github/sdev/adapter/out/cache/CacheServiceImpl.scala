package io.github.sdev.adapter.out.cache

import io.github.sdev.application.ports.out.CacheService
import io.github.sdev.scraper.News
import cats.effect.kernel.Resource
import dev.profunktor.redis4cats.algebra.StringCommands
import dev.profunktor.redis4cats.effects.SetArgs._
import dev.profunktor.redis4cats.effects.SetArg.Ttl.Px
import dev.profunktor.redis4cats.effects
import cats.effect.kernel.MonadCancel
import cats.syntax.all._

class CacheServiceImpl[F[_]](redisClient: Resource[F, StringCommands[F, String, String]], config: CacheConfig)(implicit
    F: MonadCancel[F, Throwable]
) extends CacheService[F] {

  // TODO
  override def getAll(): F[List[News]] = ???

  // TODO
  override def save(news: List[News]): F[Unit] =
    redisClient.use { command =>
      command.set("news", news.toString, effects.SetArgs(config.ttl)).void
    }
}
