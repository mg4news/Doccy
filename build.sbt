name := "Doccy"
version := "0.1"
organization := "org.mg4news"

scalaVersion := "2.13.1"
scalacOptions += "-deprecation"

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.8.0"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.1.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.0" % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.1" % "test"
libraryDependencies += "ch.rasc" %"bsoncodec" %"1.0.1"


