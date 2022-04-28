import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
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
  circeOptics,
  scalaScraper,
  doobieCore,
  doobiePostgres,
  doobieHickari,
  quill,
  doobieQuill,
  postgres,
  redis4Cats,
  pureConfig,
  pureConfigCatsEffect,
  sangria,
  sangriaCirce,
  munit            % Test,
  munitCatsEffect3 % Test,
  log4cats,
  logbackClassic % Runtime
)

testFrameworks += TestFramework("munit.Framework")
