package io.github.sdev.adapter.out.cache

import scala.concurrent.duration.FiniteDuration

case class CacheConfig(ttl: FiniteDuration)
