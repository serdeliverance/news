package io.github.sdev.application.config

import cats.effect.Async
import cats.syntax.all._

object Config {

  case class AppConfig(db: DbConfig, redis: RedisConfig, cache: CacheConfig, server: ServerConfig)

  case class DbConfig(host: String, port: Int, user: String, password: String, database: String, maxSessions: Int = 10)
  case class RedisConfig(url: String)
  case class CacheConfig(ttl: Int)
  case class ServerConfig(host: String, port: Int)

  def load[F[_]: Async]: F[AppConfig] = ???
  // (
  //   env("DB_HOST").as[String],
  //   env("DB_PORT").as[Int],
  //   env("DB_USER").as[String],
  //   env("DB_PASSWORD").as[String],
  //   env("DB_DATABASE").as[String],
  //   env("REDIS_URL").as[String],
  //   env("CACHE_TTL").as[Int],
  //   env("SERVER_HOST").as[String],
  //   env("SERVER_PORT").as[Int]
  // ).parMapN { (dbHost, dbPort, dbUser, dbPassword, dbDatabase, redisUrl, cacheTtl, serverHost, serverPort) =>
  //   val dbConfig     = DbConfig(dbHost, dbPort, dbUser, dbPassword, dbDatabase)
  //   val redisConfig  = RedisConfig(redisUrl)
  //   val cacheConfig  = CacheConfig(cacheTtl)
  //   val serverConfig = ServerConfig(serverHost, serverPort)
  //   AppConfig(dbConfig, redisConfig, cacheConfig, serverConfig)
  // }.load[F]
}
