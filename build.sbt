ThisBuild / organization := "com.michaelt"
ThisBuild / scalaVersion := "3.2.2"

// Not sure why this is needed but the overall project shows unused dependencies otherwise
unusedCompileDependenciesFilter -= moduleFilter("org.scala-lang", "scala3-library")

lazy val DeepIntegrationTest = IntegrationTest.extend(Test)
ThisBuild / DeepIntegrationTest / parallelExecution := false
inConfig(DeepIntegrationTest)(org.scalafmt.sbt.ScalafmtPlugin.scalafmtConfigSettings)

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-new-syntax",
    "-Xmax-inlines",
    "128",
  ),
  fork := true,
  // Not sure why this is needed but commonTest, tracingCommon and tracingLogging show unused
  // dependencies otherwise
  unusedCompileDependenciesFilter -= moduleFilter("org.scala-lang", "scala3-library"),
) ++ Defaults.itSettings

lazy val service = project
  .settings(
    name := "michaelt-repro",
    commonSettings,
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % Versions.CatsCore,
      "com.github.pureconfig" %% "pureconfig-core" % Versions.Pureconfig,
      "com.softwaremill.sttp.model" %% "core" % Versions.SttpModelCore,
      "io.circe" %% "circe-core" % Versions.Circe,
      "com.github.cb372" %% "cats-retry" % Versions.CatsRetry,
      "com.google.auth" % "google-auth-library-oauth2-http" % Versions.GoogleAuthLibraryOauth2Http,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-client" % Versions.Tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % Versions.Tapir,
      "org.http4s" %% "http4s-client" % Versions.Http4s,
      "ch.qos.logback" % "logback-classic" % Versions.Logback % Runtime,
      "com.google.cloud.sql" % "postgres-socket-factory" % Versions.GoogleCloudSql % Runtime,
      "com.softwaremill.sttp.tapir" %% "tapir-cats" % Versions.Tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % Versions.Tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % Versions.Tapir,
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % Versions.Tapir,
      "io.circe" %% "circe-generic" % Versions.Circe,
      "io.janstenpickle" %% "trace4cats-http4s-server" % Versions.Trace4CatsComponents,
      "net.logstash.logback" % "logstash-logback-encoder" % Versions.LogstashLogback % Runtime,
      "org.gnieh" %% "diffson-circe" % Versions.Diffson,
      "org.http4s" %% "http4s-blaze-server" % Versions.Http4sBlaze,
      "org.tpolecat" %% "doobie-hikari" % Versions.Doobie,
      "org.tpolecat" %% "doobie-postgres" % Versions.Doobie,
      "org.tpolecat" %% "doobie-postgres-circe" % Versions.Doobie,
    ),
    dockerExposedPorts := Seq(8080),
    dockerBaseImage := "eclipse-temurin:11.0.17_8-jre-focal",
  )
  .enablePlugins(DockerPlugin, GitVersioning, JavaAppPackaging, AshScriptPlugin)


