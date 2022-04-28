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
    val postgres            = "42.2.11"
    val redis4Cats          = "1.1.1"
    val pureConfig          = "0.17.1"
    val organizeImports     = "0.6.0"
    val sangria             = "3.0.0"
    val sangriaCirce        = "1.3.2"
    val doobie              = "1.0.0-RC2"
    val quill               = "3.14.1"
    val doobieQuill         = "0.0.5"
    val mockitoCore         = "3.5.13"
    val mockitoScala        = "1.16.42"
  }

  val http4sEmberServer    = "org.http4s"            %% "http4s-ember-server"    % V.http4sVersion
  val http4sCirce          = "org.http4s"            %% "http4s-circe"           % V.http4sVersion
  val http4sDsl            = "org.http4s"            %% "http4s-dsl"             % V.http4sVersion
  val circeCore            = "io.circe"              %% "circe-core"             % V.circeVersion
  val circeParser          = "io.circe"              %% "circe-parser"           % V.circeVersion
  val circeGeneric         = "io.circe"              %% "circe-generic"          % V.circeVersion
  val circeOptics          = "io.circe"              %% "circe-optics"           % V.circeVersion
  val scalaScraper         = "net.ruippeixotog"      %% "scala-scraper"          % V.scalaScraper
  val catsEffect           = "org.typelevel"         %% "cats-effect"            % V.catsEffectVersion
  val circeGenericsExtras  = "io.circe"              %% "circe-generic-extras"   % V.circeGenericsExtras
  val doobieCore           = "org.tpolecat"          %% "doobie-core"            % V.doobie
  val doobiePostgres       = "org.tpolecat"          %% "doobie-postgres"        % V.doobie
  val doobieHickari        = "org.tpolecat"          %% "doobie-hikari"          % V.doobie
  val quill                = "io.getquill"           %% "quill-jdbc"             % V.quill
  val doobieQuill          = "org.polyvariant"       %% "doobie-quill"           % V.doobieQuill
  val postgres             = "org.postgresql"         % "postgresql"             % V.postgres
  val redis4Cats           = "dev.profunktor"        %% "redis4cats-effects"     % V.redis4Cats
  val pureConfig           = "com.github.pureconfig" %% "pureconfig"             % V.pureConfig
  val pureConfigCatsEffect = "com.github.pureconfig" %% "pureconfig-cats-effect" % V.pureConfig
  val organizeImports      = "com.github.liancheng"  %% "organize-imports"       % V.organizeImports
  val sangria              = "org.sangria-graphql"   %% "sangria"                % V.sangria
  val sangriaCirce         = "org.sangria-graphql"   %% "sangria-circe"          % V.sangriaCirce

  val munit            = "org.scalameta" %% "munit"               % V.munitVersion
  val munitCatsEffect3 = "org.typelevel" %% "munit-cats-effect-3" % V.munitCatsEffect
  val mockitoCore      = "org.mockito"    % "mockito-core"        % V.mockitoCore
  val mockitoScala     = "org.mockito"   %% "mockito-scala"       % V.mockitoScala
  val logbackClassic   = "ch.qos.logback" % "logback-classic"     % V.logbackVersion
  val log4cats         = "org.typelevel" %% "log4cats-slf4j"      % V.log4catsVersion
}
