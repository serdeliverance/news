package io.github.sdev.adapter.out.persistence

import io.github.sdev.application.ports.out.NewsRepository
import io.github.sdev.adapter.out.persistence.Ops._
import io.github.sdev.scraper.News

import cats.effect._
import skunk._
import skunk.implicits._
import skunk.codec.all._
import io.github.sdev.adapter.out.persistence.models.NewsEntity
import cats.effect.kernel.Resource
import cats.syntax.all._
import cats.effect.syntax.all._
import cats.syntax.all._
import cats.Monad
import cats.Applicative

class NewsRepositoryImpl[F[_]: Monad](sessions: Resource[F, Session[F]])(implicit F: MonadCancel[F, Throwable]) extends NewsRepository[F] {

  // decoders
  private val newsEntityDecoder: Decoder[NewsEntity] =
    (varchar(50) ~ varchar(255)).gmap[NewsEntity]

  // queries ands comands
  private val findAllQuery = sql"SELECT title, link FROM headlines"
    .query(newsEntityDecoder)

  private val insertCommand = sql"INSERT INTO headlines(title, link) VALUES($varchar, $varchar)".command
    .gcontramap[NewsEntity]

  override def findAll(): F[List[News]] =
    sessions.use { session =>
      session
        .execute(findAllQuery)
        .map(entities => entities.map(e => e.toDomain))
    }

  override def save(news: News): F[Unit] =
    sessions.use { session =>
      session.prepare(insertCommand).use { command =>
        command.execute(news.toEntity).void
      }
    }

  // TODO
  override def save(news: List[News]): F[Unit] = ???
}
