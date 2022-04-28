package io.github.sdev.adapter.out.cache

import io.github.sdev.application.ports.out.CacheService
import io.github.sdev.domain.entities.News
import cats.effect.kernel.Resource
import dev.profunktor.redis4cats.algebra.StringCommands
import dev.profunktor.redis4cats.effects.SetArgs._
import dev.profunktor.redis4cats.effects.SetArg.Ttl.Px
import dev.profunktor.redis4cats.effects
import cats.effect.kernel.MonadCancel
import cats.syntax.all._
import dev.profunktor.redis4cats.RedisCommands
import org.typelevel.log4cats.Logger
import io.github.sdev.application.json.SerDes._
import io.circe.syntax._
import io.circe.parser.decode

class CacheServiceImpl[F[_]: Logger](redisCommands: RedisCommands[F, String, String], config: CacheConfig)(implicit
    F: MonadCancel[F, Throwable]
) extends CacheService[F] {

  private val NEWS_KEY = "news"

  override def getAll(): F[List[News]] =
    redisCommands
      .get(NEWS_KEY)
      .flatMap {
        case Some(result) =>
          decode[List[News]](result) match {
            case Right(news) => news.pure[F]
            case Left(err) =>
              Logger[F].error("Error decoding news from redis") *>
                List.empty[News].pure[F]
          }
        case None =>
          Logger[F].info("There is no news on cache") *> List.empty[News].pure[F]
      }

  override def save(news: List[News]): F[Unit] =
    redisCommands.set(NEWS_KEY, news.asJson.noSpaces, effects.SetArgs(config.ttl)).void
}
