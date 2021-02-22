import sbt.Keys.{crossSbtVersions, homepage}

lazy val `sbt-liquibase` = (project in file("."))
  .settings(inThisBuild(List(
    sbtPlugin := true,
    organization := "se.sambera",
    scalacOptions := Seq("-unchecked", "-deprecation", "-feature"),
    licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    homepage := Some(url("https://github.com/sambera/sbt-liquibase"))
  )),
    name := "sbt-liquibase",
    version := "1.0.3", // WARNING: Manual release handling
    publishArtifact in Test := false,
    publishMavenStyle := true,
    publishTo := Some(Resolver.file("sambera.se", new File(Path.userHome.absolutePath + "/repo/sambera-releases/releases"))),
    libraryDependencies ++= Seq(
      "org.liquibase" % "liquibase-core" % "3.6.3",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    ),
    crossScalaVersions := Seq("2.10.7", "2.12.12"),
    crossSbtVersions := Vector("0.13.18", "1.4.7")
  )
