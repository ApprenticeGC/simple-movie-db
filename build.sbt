name := "o-john"

version := "0.1.0"

scalaVersion := "2.11.1"

resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "spray repo" at "http://repo.spray.io",
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.5",
  "io.spray" %% "spray-can" % "1.3.1",
  "io.spray" %% "spray-routing" % "1.3.1",
  "io.spray" %% "spray-http" % "1.3.1",
  "io.spray" %% "spray-httpx" % "1.3.1",
  "com.typesafe.play" %% "play-json" % "2.3.3",
  "org.reactivemongo" %% "reactivemongo" % "0.11.0-SNAPSHOT",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.0-SNAPSHOT",
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
)