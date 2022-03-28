import sbt._

object Dependencies {

  object V {
    val http4sVersion          = "0.23.11"
    val circeVersion           = "0.14.1"
    val munitVersion           = "0.7.29"
    val logbackVersion         = "1.2.10"
    val munitCatsEffectVersion = "1.0.7"
    val scalaScraperVersion    = "2.2.0"
    val catsEffectVersion      = "3.3.9"
  }

  val http4sEmberServer = "org.http4s"      %% "http4s-ember-server" % V.http4sVersion
  val http4sEmberClient = "org.http4s"      %% "http4s-ember-client" % V.http4sVersion
  val http4sCirce       = "org.http4s"      %% "http4s-circe"        % V.http4sVersion
  val http4sDsl         = "org.http4s"      %% "http4s-dsl"          % V.http4sVersion
  val circeGeneric      = "io.circe"        %% "circe-generic"       % V.circeVersion
  val scalaScraper      = "net.ruippeixotog" % "scala-scraper"       % V.scalaScraperVersion
  val catsEffect        = "org.typelevel"   %% "cats-effect"         % V.catsEffectVersion
  val munit             = "org.scalameta"   %% "munit"               % V.munitVersion
  val munitCatsEffect3  = "org.typelevel"   %% "munit-cats-effect-3" % V.munitCatsEffectVersion
  val logbackClassic    = "ch.qos.logback"   % "logback-classic"     % V.logbackVersion
}
