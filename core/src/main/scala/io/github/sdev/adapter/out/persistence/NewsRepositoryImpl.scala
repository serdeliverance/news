package io.github.sdev.adapter.out.persistence

import io.github.sdev.application.ports.out.NewsRepository
import io.github.sdev.adapter.out.persistence.Ops._
import io.github.sdev.scraper.News
import cats.effect.IO

import skunk._
import skunk.implicits._
import skunk.codec.all._
import io.github.sdev.adapter.out.persistence.models.NewsEntity
import cats.effect.kernel.Resource

class NewsRepositoryImpl(sessions: Resource[IO, Session[IO]]) extends NewsRepository {

  // decoders
  private val newsEntityDecoder: Decoder[NewsEntity] =
    (varchar(50) ~ varchar(255)).gmap[NewsEntity]

  // queries ands comands
  private val findAllQuery = sql"SELECT title, link FROM headlines"
    .query(newsEntityDecoder)

  private val insertCommand = sql"INSERT INTO headlines(title, link) VALUES($varchar, $varchar)".command
    .gcontramap[NewsEntity]

  override def findAll(): IO[List[News]] =
    sessions.use { session =>
      session
        .execute(findAllQuery)
        .map(entities => entities.map(e => e.toDomain))
    }

  override def save(news: News): IO[Unit] =
    sessions.use { session =>
      session.prepare(insertCommand).use { command =>
        command.execute(news.toEntity).void
      }
    }

}
