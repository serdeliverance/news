package io.github.sdev.stubs

import io.github.sdev.domain.entities.News

trait NewsStubs {

  val aListOfNews = List(News("A new", "https://nytimes.com/a-new"), News("Another new", "https://nytimes.com/another-new"))
}
