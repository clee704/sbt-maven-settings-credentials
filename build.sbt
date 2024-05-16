organization := "dev.chungmin"
name := "sbt-maven-settings-credentials"

enablePlugins(SbtPlugin)
pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" => "1.9.9"
  }
}

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

// Publish config
credentials += Credentials(
  "Sonatype Nexus Repository Manager",
  "oss.sonatype.org",
  sys.env.getOrElse("SONATYPE_USERNAME", ""),
  sys.env.getOrElse("SONATYPE_PASSWORD", "")
)
scmInfo := Some(
  ScmInfo(
    url("https://github.com/clee704/sbt-maven-settings-credentials"),
    "scm:git@github.com:clee704/sbt-maven-settings-credentials.git"
  )
)
developers := List(
  Developer(
    id = "clee704",
    name = "Chungmin Lee",
    email = "lee@chungmin.dev",
    url = url("https://github.com/clee704")
  )
)
description := "Read Maven settings.xml to create credentials for Maven repos in resolvers"
licenses := List(
  "Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")
)
homepage := Some(url("https://github.com/clee704/sbt-maven-settings-credentials"))
pomIncludeRepository := { _ => false }
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
publishMavenStyle := true
