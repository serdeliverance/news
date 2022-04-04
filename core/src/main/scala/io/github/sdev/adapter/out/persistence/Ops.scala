package io.github.sdev.adapter.out.persistence

import io.github.sdev.adapter.out.persistence.models.NewsEntity
import io.github.sdev.scraper.News

object Ops {

  implicit class NewsEntityOps(entity: NewsEntity) {
    def toDomain: News =
      News(entity.title, entity.link)
  }

  implicit class NewsOps(news: News) {
    def toEntity: NewsEntity =
      NewsEntity(news.title, news.link)
  }
}
