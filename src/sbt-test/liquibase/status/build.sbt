import scala.sys.process.Process
import se.sambera.sbt.plugins.LiquibasePlugin

val userName = "testuser"
val password = "pwd"
val h2Driver = "org.h2.Driver"

lazy val test = (project in file("."))
  .enablePlugins(LiquibasePlugin)
  .settings(
    scalaVersion := "2.12.12",
    name := "sbt-liquibase-test-status",
    organization := "se.sambera",
    version := "1.0.1",

    libraryDependencies += "com.h2database" % "h2" % "1.4.197",

    liquibaseUsername := userName,
    liquibasePassword := password,
    liquibaseDriver := h2Driver,
    liquibaseUrl := s"jdbc:h2:file:${target.value / "test"};INIT=CREATE SCHEMA IF NOT EXISTS TEST;",
    liquibaseChangelog := "src/main/database/changelog-master.xml",

    TaskKey[Unit]("check") := {
      println("Checking result of liquibaseStatus")
      val process = Process("cat", Seq("target/unRunChangeSets.txt"))
      val out = (process !!)

      if (!out.contains("1 change sets have not been applied to") && !out.contains("src/main/database/changelog-2.0.xml::1417310123172-2::(generated)")) sys.error("### Unexpected output: " + out)
    }
  )
