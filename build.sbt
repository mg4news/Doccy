// Library versions

val ScalacticVersion  = "3.1.1"
val ScalatestVersion  = "3.1.1"
val ScalacheckVersion = "1.14.3"
val Http4sVersion     = "0.21.3"
val CirceVersion      = "0.13.0"
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
    "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0",
    "ch.rasc"           % "bsoncodec"            % "1.0.1",

    "org.scalactic"     %% "scalactic"           % ScalacticVersion,
    "org.scalatest"     %% "scalatest"           % ScalatestVersion % "test",
    "org.scalacheck"    %% "scalacheck"          % ScalacheckVersion % "test",
    "org.http4s"        %% "http4s-blaze-server" % Http4sVersion,
    "org.http4s"        %% "http4s-blaze-client" % Http4sVersion,
    "org.http4s"        %% "http4s-circe"        % Http4sVersion,
    "org.http4s"        %% "http4s-dsl"          % Http4sVersion,
    "io.circe"          %% "circe-generic"       % CirceVersion,
    "org.specs2"        %% "specs2-core"         % Specs2Version % "test",
    "ch.qos.logback"    %  "logback-classic"     % LogbackVersion
  ),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % KindProjectorVersion cross CrossVersion.full),
  addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % BetterMonadicVersion)
)

//val MongoVersion      = "2.9.0"
//val BsonCodecVersion  = "1.0.1"
//"org.typelevel"     %% "cats-core"           % CatsVersion,
//libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % MongoVersion
//libraryDependencies += "org.scalactic" %% "scalactic" % ScalacticVersion
//libraryDependencies += "org.scalatest" %% "scalatest" % ScalatestVersion % "test"
//libraryDependencies += "org.scalacheck" %% "scalacheck" % ScalacheckVersion % "test"
//libraryDependencies += "ch.rasc" % "bsoncodec" % BsonCodecVersion
//     "ch.rasc"           % "bsoncodec"            % BsonCodecVersion,
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings"
)
