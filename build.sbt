name := """mint-oai"""
version := "1.0.0"
scalaVersion := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

routesGenerator := InjectedRoutesGenerator

val appDependencies = Seq(
  // Add your project dependencies here,
)

libraryDependencies ++= Seq(
  // Add your project dependencies here,
  "org.json" % "json" % "20160212",
  "org.mongodb" % "mongo-java-driver" % "2.8.0"
)