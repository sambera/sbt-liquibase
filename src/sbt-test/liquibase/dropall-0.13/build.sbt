import java.sql.{Connection, DriverManager}

val userName = "testuser"
val password = "pwd"
val h2Driver = "org.h2.Driver"

scalaVersion := "2.12.12"
name := "sbt-liquibase-test-dropall"
organization := "se.sambera"
version := "1.0.5"

libraryDependencies += "com.h2database" % "h2" % "1.4.197"

liquibaseUsername := userName
liquibasePassword := password
liquibaseDriver := h2Driver
liquibaseUrl := s"jdbc:h2:file:${target.value / "test"};INIT=CREATE SCHEMA IF NOT EXISTS TEST;"
liquibaseChangelog := "src/main/database/changelog-master.xml"

TaskKey[Unit]("checkTableExist") := {
  var connection: Connection = null
  try {
    println("Checking state of the database after 'liquibaseUpdate'.")
    Class.forName(h2Driver)
    val url = s"jdbc:h2:file:${target.value / "test"};"
    connection = DriverManager.getConnection(url, userName, password)
    val tableTypes = Array("TABLE")
    val rs = connection.getMetaData.getTables(null, null, "%", tableTypes)
    val expectedTables = Seq("LOCATION", "PERSON", "ADDRESS", "DATABASECHANGELOG", "DATABASECHANGELOGLOCK")
    if (!rs.next()) {
      sys.error(s"No tables found. Expected ${expectedTables} tables to exist after Liquibase update.")
    } else {
      var actualTables: Seq[String] = Seq.empty[String]
      do {
        var tableName = rs.getString("TABLE_NAME")
        actualTables = actualTables :+ tableName
      } while (rs.next())

      if (!expectedTables.diff(actualTables).isEmpty || !actualTables.diff(expectedTables).isEmpty) {
        sys.error(s"Not the same number of tables. Expected tables: ${expectedTables} - Actual tables ${actualTables}")
      }
    }
  } catch {
    case e: Exception => e.printStackTrace()

      sys.error(e.printStackTrace().toString)
  } finally {
    if (connection != null)
      connection.close()
  }
}

TaskKey[Unit]("checkTablesAreDrop") := {
  var connection: Connection = null
  try {
    println("Checking state of the database after 'liquibaseDropAll'.")
    Class.forName(h2Driver)
    val url = s"jdbc:h2:file:${target.value / "test"};"
    connection = DriverManager.getConnection(url, userName, password)
    val tableTypes = Array("TABLE")
    val rs = connection.getMetaData.getTables(null, null, "%", tableTypes)
    if (rs.next()) {
      var actualTables: Seq[String] = Seq.empty[String]
      do {
        var tableName = rs.getString("TABLE_NAME")
        actualTables = actualTables :+ tableName
      } while (rs.next())

      sys.error(s"Expected no tables to exist. Found ${actualTables}.")
    }
  } catch {
    case e: Exception => e.printStackTrace()
      sys.error(e.printStackTrace().toString)
  } finally {
    if (connection != null)
      connection.close()
  }
}
