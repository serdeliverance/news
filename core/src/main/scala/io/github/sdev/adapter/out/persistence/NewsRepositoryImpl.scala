package io.github.sdev.adapter.out.persistence

import io.github.sdev.application.json.ports.out.NewsRepository
import io.github.sdev.adapter.out.persistence.Ops._
import io.github.sdev.scraper.News
import cats.effect.IO

import skunk._
import skunk.implicits._
import skunk.codec.all._
import io.github.sdev.adapter.out.persistence.models.NewsEntity
import cats.effect.kernel.Resource

class NewsRepositoryImpl(sessions: Resource[IO, Session[IO]]) extends NewsRepository {

  private val findAllQuery = sql"SELECT title, link FROM news"
    .query(varchar ~ varchar)
    .map { case title ~ link => NewsEntity(title, link) }

  private val insert = sql"INSERT INTO news(title, link) VALUES($varchar, $varchar)".command
    .gcontramap[NewsEntity]

  override def findAll(): IO[List[News]] =
    sessions.use { session =>
      session
        .execute(findAllQuery)
        .map(entities => entities.map(e => e.toDomain))
    }

  override def save(news: News): IO[Unit] = ???

}
