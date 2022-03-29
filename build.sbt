import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "io.github.sdev"
ThisBuild / organizationName := "sdev"

lazy val core = (project in file("core"))
  .dependsOn(scraper)
  .settings(coreDependencies)

lazy val scraper = (project in file("scraper"))
  .settings(scraperDependencies)

lazy val scraperDependencies = libraryDependencies ++= Seq(
  catsEffect,
  scalaScraper
)

lazy val coreDependencies = libraryDependencies ++= Seq(
  http4sEmberClient,
  http4sEmberServer,
  http4sCirce,
  http4sDsl,
  circeGeneric,
  circeGenericsExtras,
  munit            % Test,
  munitCatsEffect3 % Test,
  logbackClassic   % Runtime
)

testFrameworks += TestFramework("munit.Framework")
