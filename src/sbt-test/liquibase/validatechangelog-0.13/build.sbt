import se.sambera.sbt.plugins.LiquibasePlugin

val userName = "testuser"
val password = "pwd"
val h2Driver = "org.h2.Driver"

scalaVersion := "2.12.12"
name := "sbt-liquibase-test-validate"
organization := "se.sambera"
version := "1.0.1"

libraryDependencies += "com.h2database" % "h2" % "1.4.197"

liquibaseUsername := userName
liquibasePassword := password
liquibaseDriver := h2Driver
liquibaseUrl := s"jdbc:h2:file:${target.value / "test"};INIT=CREATE SCHEMA IF NOT EXISTS TEST;"
liquibaseChangelog := "src/main/database/changelog-master.xml"
