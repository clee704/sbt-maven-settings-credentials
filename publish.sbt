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

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true
sbtPluginPublishLegacyMavenStyle := false
