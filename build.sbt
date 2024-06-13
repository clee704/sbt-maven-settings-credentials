scalaVersion := "2.12.19"
scalacOptions ++= Seq("-release", "8")
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

enablePlugins(SbtPlugin)

pluginCrossBuild / sbtVersion := {
  scalaBinaryVersion.value match {
    case "2.12" => "1.3.0"
  }
}
