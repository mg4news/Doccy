// Library versions

val ScalatestVersion  = "3.1.1"
val ScalacheckVersion = "1.14.3"
val LogbackVersion    = "1.2.3"

// Plugin versions
val KindProjectorVersion = "0.11.0"
val BetterMonadicVersion = "0.3.1"

// Scala language version
val ScalaVersion = "2.13.2"

lazy val root = (project in file(".")).settings(
  organization := "org.mg4news",
  name := "Doccy",
  version := "0.2.0",
  scalaVersion := ScalaVersion,
  libraryDependencies ++= Seq(
    // Main application stuff
    "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0",
    "ch.rasc"           % "bsoncodec"           % "1.0.1",
    "com.typesafe"      % "config"              % "1.4.0",
    "ch.qos.logback"    % "logback-classic"     % LogbackVersion,

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
