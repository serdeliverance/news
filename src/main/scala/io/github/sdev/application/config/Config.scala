package io.github.sdev.application.config

import cats.effect.Async
import cats.syntax.all._
import scala.concurrent.duration.FiniteDuration

object Config {

  case class AppConfig(db: DbConfig, redis: RedisConfig, cache: CacheConfig, server: ServerConfig)

  case class DbConfig(host: String, port: Int, user: String, password: String, database: String, maxSessions: Int)
  case class RedisConfig(url: String)
  case class CacheConfig(ttl: FiniteDuration)
  case class ServerConfig(host: String, port: Int)
}
