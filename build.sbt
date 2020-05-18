// Library versions

val ScalatestVersion    = "3.1.1"
val ScalacheckVersion   = "1.14.3"
val LogbackVersion      = "1.2.3"
val ScalaLoggingVersion = "3.9.2"
val ArgonautVersion     = "6.3.0"
val AkkaHttpVersion     = "10.1.12"
val AkkaStreamVersion   = "2.6.5"

// Scala language version
val ScalaVersion = "2.13.2"

lazy val root = (project in file(".")).settings(
  organization := "org.mg4news",
  name := "Doccy",
  version := "0.2.0",
  scalaVersion := ScalaVersion,
  libraryDependencies ++= Seq(
    // Main application stuff
    "org.mongodb.scala"          %% "mongo-scala-driver" % "2.9.0",
    "ch.rasc"                    % "bsoncodec"           % "1.0.1",
    "com.typesafe"               % "config"              % "1.4.0",
    "ch.qos.logback"             % "logback-classic"     % LogbackVersion,
    "com.typesafe.scala-logging" %% "scala-logging"      % ScalaLoggingVersion,

    // JSON, HTTP, etc stuff
    "io.argonaut"       %% "argonaut"           % ArgonautVersion,
    "de.heikoseeberger" %% "akka-http-argonaut" % "1.32.0",
    "com.typesafe.akka" %% "akka-http"          % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream"        % AkkaStreamVersion,
    "com.typesafe.akka" %% "akka-http-testkit"  % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-testkit"       % "2.6.5",

    // My test dependencies
    "org.scalatest"     %% "scalatest"          % ScalatestVersion % "test",
    "org.scalacheck"    %% "scalacheck"         % ScalacheckVersion % "test"
  ),
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings"
)
