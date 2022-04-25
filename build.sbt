import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "io.github.sdev"
ThisBuild / organizationName := "sdev"

ThisBuild / libraryDependencySchemes += "org.typelevel" %% "cats-effect" % "always"

lazy val core = (project in file("core"))
  .settings(coreDependencies)

lazy val playground = (project in file("playground"))
  .dependsOn(core)
  .settings(playgroundDependencies)

lazy val coreDependencies = libraryDependencies ++= Seq(
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

lazy val playgroundDependencies =
  libraryDependencies ++= Seq(
    catsEffect,
    skunk,
    postgres
  )

testFrameworks += TestFramework("munit.Framework")
