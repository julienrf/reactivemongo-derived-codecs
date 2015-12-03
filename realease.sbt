pomExtra := (
    <url>http://github.com/julienrf/reactivemongo-derived-codecs</url>
    <licenses>
    <license>
        <name>MIT License</name>
        <url>http://opensource.org/licenses/mit-license.php</url>
    </license>
    </licenses>
    <scm>
    <url>git@github.com:julienrf/reactivemongo-derived-codecs.git</url>
    <connection>scm:git:git@github.com:julienrf/reactivemongo-derived-codecs.git</connection>
    </scm>
    <developers>
    <developer>
        <id>julienrf</id>
        <name>Julien Richard-Foy</name>
        <url>http://julien.richard-foy.fr</url>
    </developer>
    </developers>
)

sonatypeProfileName := "org.julienrf"

import ReleaseTransformations._

releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    ReleaseStep(action = Command.process("publishSigned", _)),
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),
    pushChanges
)