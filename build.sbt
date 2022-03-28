import Dependencies._

ThisBuild / scalaVersion     := "3.1.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "io.github.sdev"
ThisBuild / organizationName := "sdev"

val Http4sVersion          = "0.23.11"
val CirceVersion           = "0.14.1"
val MunitVersion           = "0.7.29"
val LogbackVersion         = "1.2.10"
val MunitCatsEffectVersion = "1.0.7"

lazy val root = (project in file("."))
  .settings(
    name := "avantstay-challenge"
  )

libraryDependencies ++= Seq(
  "org.http4s"    %% "http4s-ember-server" % Http4sVersion,
  "org.http4s"    %% "http4s-ember-client" % Http4sVersion,
  "org.http4s"    %% "http4s-circe"        % Http4sVersion,
  "org.http4s"    %% "http4s-dsl"          % Http4sVersion,
  "io.circe"      %% "circe-generic"       % CirceVersion,
  "org.scalameta" %% "munit"               % MunitVersion           % Test,
  "org.typelevel" %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
  "ch.qos.logback" % "logback-classic"     % LogbackVersion         % Runtime
)

testFrameworks += TestFramework("munit.Framework")
