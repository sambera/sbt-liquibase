sbtPlugin := true

organization := "se.sambera"
name := "sbt-liquibase"
version := "0.1.0"

licenses := Seq("Apache License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))
homepage := Some(url("https://github.com/sambera/sbt-liquibase"))

scalaVersion := "2.12.6"

val liquibaseCore = "org.liquibase" % "liquibase-core" % "3.6.1"

libraryDependencies += liquibaseCore

scriptedLaunchOpts := { scriptedLaunchOpts.value ++
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
}
scriptedBufferLog := false

// Create a folder in 'Path.userHome.absolutePath + /<local_git_repository>'.
// This will publish the plugin to a local repository (local git-repository)
// Commit and push to the remote git repository.
// ##### Adds plugin and sambera-release resolver in 'project/plugins.sbt'
// ##### resolvers += Resolver.url("<organization>-releases", url("https://github.com/<remote_git_repository>/releases/tree/master"))
// ##### addSbtPlugin("se.sambera" % "sbt-liquibase" % "0.1.0")
publishTo := Some(Resolver.file("sambera.se", new File(Path.userHome.absolutePath + "/repo/sambera-releases/releases")))
