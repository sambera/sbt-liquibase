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
    publishArtifact in Test := false,
    publishMavenStyle := false,
    publishTo := Some(Resolver.file("sambera.se", new File(Path.userHome.absolutePath + "/repo/sambera-releases/releases"))),
    libraryDependencies ++= Seq(
      "org.liquibase" % "liquibase-core" % "3.6.1"
    ),
    crossScalaVersions := Seq("2.10.7", "2.12.6"),
    crossSbtVersions := Vector("0.13.17", "1.1.6")
  )

// Create a folder in 'Path.userHome.absolutePath + /<local_git_repository>'.
// The 'sbt publish' command will publish the plugin to a local repository (local git-repository)

// Commit and push to the remote git repository, e.g. https://github.com/<organization>/releases.
// To Use this plugin:
// ##### Adds plugin and resolver in 'project/plugins.sbt'
// ##### e.g.
// ##### resolvers += "<organization>-github" at "https://raw.githubusercontent.com/<organization>/releases/master/"
// ##### addSbtPlugin("<organization>" % "<name>" % "<version>")
// ##### See README as an example
