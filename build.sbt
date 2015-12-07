name := "reactivemongo-derived-codecs"

organization := "org.julienrf"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.2.5",
  "org.reactivemongo" %% "reactivemongo-bson" % "0.11.7",
  "org.julienrf" %% "enum-labels" % "1.0",
  "org.scalacheck" %% "scalacheck" % "1.12.5" % Test,
  "org.scalatest" %% "scalatest" % "2.2.4" % Test
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture"
)