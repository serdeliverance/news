package io.github.sdev.adapter.out.persistence

import io.github.sdev.application.ports.out.NewsRepository
import io.github.sdev.domain.entities.News

import cats.effect._
import io.github.sdev.adapter.out.persistence.models.NewsEntity
import cats.effect.syntax.all._
import cats.syntax.all._
import org.polyvariant.doobiequill.DoobieContext
import doobie.implicits._
import io.getquill.{ idiom => _ }
import io.getquill._
import doobie.util.transactor.Transactor
import Ops.NewsOps

class NewsRepositoryImpl[F[_]](xa: Transactor[F])(implicit F: MonadCancel[F, Throwable]) extends NewsRepository[F] {

  private val db = new DoobieContext.Postgres(Literal)
  import db._

  private final val TABLE_NAME = "headlines"

  // TODO refactor to avoid duplication of code
  override def save(news: News): F[Unit] = {
    val insertQuote = quote {
      querySchema[NewsEntity](TABLE_NAME).insertValue(lift(news.toEntity))
    }

    // TODO add error handling with logging
    run(insertQuote).transact(xa).map(_ => ())
  }

  override def save(news: List[News]): F[Unit] = {
    val entities = news.map(_.toEntity)
    val bulkInsertQuote = quote {
      liftQuery(entities).foreach(n => querySchema[NewsEntity](TABLE_NAME).insertValue(n))
    }

    // TODO add error handling with logging
    run(bulkInsertQuote).transact(xa).map(_ => ())
  }
}
