import Util._

Global / onChangedBuildSource := ReloadOnSourceChanges

Global / excludeLintKeys ++= Set(
  evictionWarningOptions
)

Test / parallelExecution := false

ThisBuild / includePluginResolvers := true

ThisBuild / scalacOptions ++= Seq("-unchecked", "-deprecation")

ThisBuild / watchBeforeCommand           := Watch.clearScreen
ThisBuild / watchTriggeredMessage        := Watch.clearScreenOnTrigger
ThisBuild / watchForceTriggerOnAnyChange := true

ThisBuild / shellPrompt := { state => s"${prompt(projectName(state))}> " }
ThisBuild / watchStartMessage := { case (iteration, ProjectRef(build, projectName), commands) =>
  Some {
    s"""|~${commands.map(styled).mkString(";")}
          |Monitoring source files for ${prompt(projectName)}...""".stripMargin
  }
}
