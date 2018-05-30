sbtPlugin := true

organization := "se.sambera"
name := "sbt-liquibase"
version := "0.0.1"

licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
homepage := Some(url("https://github.com/sambera/sbt-liquibase"))

scalaVersion := "2.12.6"

val liquibaseCore = "org.liquibase" % "liquibase-core" % "3.6.1"

libraryDependencies += liquibaseCore

scriptedLaunchOpts := { scriptedLaunchOpts.value ++
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
}
scriptedBufferLog := false