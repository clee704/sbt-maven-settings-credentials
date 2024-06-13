organization := "dev.chungmin"

name := "sbt-maven-settings-credentials"

sbtPlugin := true

description := "Read Maven settings.xml to create credentials for Maven repos in resolvers"

homepage := Some(url("https://github.com/clee704/sbt-maven-settings-credentials"))

licenses := List(
  "Apache 2" -> new URI("http://www.apache.org/licenses/LICENSE-2.0.txt").toURL
)

developers := List(
  Developer(
    id = "clee704",
    name = "Chungmin Lee",
    email = "lee@chungmin.dev",
    url = url("https://github.com/clee704")
  )
)
