package io.github.sdev.adapter.out.cache

import scala.concurrent.duration.FiniteDuration
import dev.profunktor.redis4cats.effects

import scala.concurrent.duration._

case class CacheConfig(ttl: effects.SetArg.Ttl.Px)

object CacheConfig {

  def apply(ttl: Int): CacheConfig = new CacheConfig(effects.SetArg.Ttl.Px(ttl.millis))
}
