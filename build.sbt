name := """mintoai"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

val appDependencies = Seq(
  // Add your project dependencies here,
)

libraryDependencies ++= Seq(
  // Add your project dependencies here,
  "org.json" % "json" % "20160212",
  "org.mongodb" % "mongo-java-driver" % "2.8.0"
)