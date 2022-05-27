package io.github.sdev.application.config

import scala.concurrent.duration.FiniteDuration

object Config {
  case class AppConfig(
      db: DbConfig,
      redis: RedisConfig,
      cache: CacheConfig,
      server: ServerConfig,
      scraper: ScraperConfig
  )

  case class DbConfig(
      driver: String,
      url: String,
      user: String,
      password: String
  )
  case class RedisConfig(url: String)
  case class CacheConfig(ttl: FiniteDuration)
  case class ServerConfig(host: String, port: Int)
  case class ScraperConfig(url: String)
}
