// Library versions

val ScalatestVersion  = "3.1.1"
val ScalacheckVersion = "1.14.3"
val Http4sVersion     = "0.21.3"
val CatsVersion       = "2.1.3"
val ZioVersion        = "1.0.0-RC18-2"
val ZioCatsVersion    = "2.0.0.0-RC12"
val Specs2Version     = "4.9.3"
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

    // My test dependencies
    "org.scalatest"     %% "scalatest"           % ScalatestVersion % "test",
    "org.scalacheck"    %% "scalacheck"          % ScalacheckVersion % "test",



    // Dependencies for a ZIO HTTP4S service
    "org.typelevel"     %% "cats-effect"         % CatsVersion,
    "dev.zio"           %% "zio"                 % ZioVersion,
    "dev.zio"           %% "zio-interop-cats"    % ZioCatsVersion,
    "org.http4s"        %% "http4s-blaze-server" % Http4sVersion,
    "org.http4s"        %% "http4s-circe"        % Http4sVersion,
    "org.http4s"        %% "http4s-dsl"          % Http4sVersion,
    "ch.qos.logback"    %  "logback-classic"     % LogbackVersion
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
