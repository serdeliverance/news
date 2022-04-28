package io.github.sdev.adapter.out.persistence

import io.github.sdev.application.ports.out.NewsRepository
import io.github.sdev.adapter.out.persistence.Ops._
import io.github.sdev.domain.entities.News

import cats.effect._
import io.github.sdev.adapter.out.persistence.models.NewsEntity
import cats.effect.kernel.Resource
import cats.syntax.all._
import cats.effect.syntax.all._
import cats.syntax.all._
import org.polyvariant.doobiequill.DoobieContext
import doobie._
import doobie.implicits._
import io.getquill.{ idiom => _, _ }
import io.getquill._
import doobie.util.transactor.Transactor
import cats.Applicative

class NewsRepositoryImpl[F[_]](xa: Transactor[F])(implicit F: MonadCancel[F, Throwable]) extends NewsRepository[F] {

  private val db = new DoobieContext.Postgres(Literal)
  import db._

  // TODO refactor to avoid duplication of code
  override def save(news: News): F[Unit] = {
    val insertQuote = quote {
      query[News].insertValue(lift(news))
    }

    // TODO add error handling with logging
    // TODO refactor conversion to unit (()), using map is not very expressive
    run(insertQuote).transact(xa).map(_ => ())
  }

  override def save(news: List[News]): F[Unit] = {
    val bulkInsertQuote = quote {
      liftQuery(news).foreach(n => query[News].insertValue(n))
    }

    // TODO add error handling with logging
    // TODO refactor conversion to unit (()), using map is not very expressive
    run(bulkInsertQuote).transact(xa).map(_ => ())
  }

}
