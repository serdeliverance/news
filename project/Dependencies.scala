import sbt._

object Dependencies {

  object V {
    val http4sVersion       = "0.23.11"
    val circeVersion        = "0.14.1"
    val munitVersion        = "0.7.29"
    val logbackVersion      = "1.2.10"
    val log4catsVersion     = "2.2.0"
    val munitCatsEffect     = "1.0.7"
    val scalaScraper        = "2.2.0"
    val catsEffectVersion   = "3.3.9"
    val circeGenericsExtras = "0.14.1"
    val skunk               = "0.2.3"
    val postgres            = "42.2.11"
    val redis4Cats          = "1.1.1"
  }

  val http4sEmberServer   = "org.http4s"       %% "http4s-ember-server"  % V.http4sVersion
  val http4sCirce         = "org.http4s"       %% "http4s-circe"         % V.http4sVersion
  val http4sDsl           = "org.http4s"       %% "http4s-dsl"           % V.http4sVersion
  val circeGeneric        = "io.circe"         %% "circe-generic"        % V.circeVersion
  val scalaScraper        = "net.ruippeixotog" %% "scala-scraper"        % V.scalaScraper
  val catsEffect          = "org.typelevel"    %% "cats-effect"          % V.catsEffectVersion
  val circeGenericsExtras = "io.circe"         %% "circe-generic-extras" % V.circeGenericsExtras
  val skunk               = "org.tpolecat"     %% "skunk-core"           % V.skunk
  val postgres            = "org.postgresql"    % "postgresql"           % V.postgres
  val redis4Cats          = "dev.profunktor"   %% "redis4cats-effects"   % V.redis4Cats

  val munit            = "org.scalameta" %% "munit"               % V.munitVersion
  val munitCatsEffect3 = "org.typelevel" %% "munit-cats-effect-3" % V.munitCatsEffect
  val logbackClassic   = "ch.qos.logback" % "logback-classic"     % V.logbackVersion
  val log4cats         = "org.typelevel" %% "log4cats-slf4j"      % V.log4catsVersion
}
