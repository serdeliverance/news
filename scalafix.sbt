import Dependencies._

ThisBuild / scalafixScalaBinaryVersion := scalaBinaryVersion.value
ThisBuild / scalafixDependencies ++= Seq(
  organizeImports
)

ThisBuild / semanticdbEnabled := true
ThisBuild / semanticdbVersion := scalafixSemanticdb.revision
