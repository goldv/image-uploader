import sbtassembly.AssemblyKeys


name := "image-uploader"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-ws" % "2.3.9" exclude("org.slf4j", "jcl-over-slf4j"),
  "com.amazonaws" % "aws-java-sdk-s3" % "1.9.0",
  "commons-io" % "commons-io" % "2.4"
)

libraryDependencies ~= { _ map {
  case m if m.organization == "com.typesafe.play" =>
    m.exclude("commons-logging", "commons-logging").
      exclude("com.typesafe.play", "build-link")
  case m => m
}}