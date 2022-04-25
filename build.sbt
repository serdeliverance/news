import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "io.github.sdev"
ThisBuild / organizationName := "sdev"

ThisBuild / libraryDependencySchemes += "org.typelevel" %% "cats-effect" % "always"

lazy val root = (project in file("."))
  .settings(dependencies)

lazy val dependencies = libraryDependencies ++= Seq(
  catsEffect,
  http4sEmberServer,
  http4sCirce,
  http4sDsl,
  circeCore,
  circeParser,
  circeGeneric,
  circeGenericsExtras,
  scalaScraper,
  skunk,
  postgres,
  redis4Cats,
  munit            % Test,
  munitCatsEffect3 % Test,
  log4cats,
  logbackClassic % Runtime
)

testFrameworks += TestFramework("munit.Framework")
