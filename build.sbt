name := "reactivemongo-derived-codecs"

organization := "org.julienrf"

scalaVersion := "2.11.8"

crossScalaVersions := Seq("2.11.8", "2.12.1")

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.reactivemongo" %% "reactivemongo-bson" % "0.12.2" % Provided,
  "org.julienrf" %% "enum-labels" % "3.1",
  "org.scalacheck" %% "scalacheck" % "1.13.5" % Test,
  "org.scalatest" %% "scalatest" % "3.0.3" % Test
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

scalacOptions in (Compile, doc) ++= Seq(
  "-doc-source-url", s"https://github.com/julienrf/${name.value}/tree/v${version.value}€{FILE_PATH}.scala",
  "-sourcepath", baseDirectory.in(LocalRootProject).value.getAbsolutePath
)

apiURL := Some(url(s"http://julienrf.github.io/${name.value}/${version.value}/api/"))

autoAPIMappings := true

val publishDoc = taskKey[Unit]("Publish API documentation")

publishDoc := {
  IO.copyDirectory((doc in Compile).value, Path.userHome / "sites" / "julienrf.github.com" / name.value / version.value / "api")
}

homepage := Some(url(s"https://github.com/julienrf/${name.value}"))

licenses := Seq("MIT License" -> url("http://opensource.org/licenses/mit-license.php"))

scmInfo := Some(
  ScmInfo(
    url(s"https://github.com/julienrf/${name.value}"),
    s"scm:git:git@github.com:julienrf/${name.value}.git"
  )
)

pomExtra :=
  <developers>
    <developer>
      <id>julienrf</id>
      <name>Julien Richard-Foy</name>
      <url>http://julien.richard-foy.fr</url>
    </developer>
  </developers>

releasePublishArtifactsAction := PgpKeys.publishSigned.value

import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  ReleaseStep(action = Command.process("publishDoc", _)),
  setNextVersion,
  commitNextVersion,
  ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
  pushChanges
)
