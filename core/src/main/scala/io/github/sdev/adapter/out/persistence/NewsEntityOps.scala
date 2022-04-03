package io.github.sdev.adapter.out.persistence

import io.github.sdev.adapter.out.persistence.models.NewsEntity
import io.github.sdev.scraper.News

object NewsEntityOps {

  implicit class NewsEntityOps(entity: NewsEntity) {
    def toDomain: News =
      News(entity.title, entity.link)
  }
}
