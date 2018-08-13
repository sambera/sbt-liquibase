import java.sql.{Connection, DriverManager}

val userName = "testuser"
val password = "pwd"
val h2Driver = "org.h2.Driver"

def checkContainsTables(expectedTables: Seq[String], target: String): Unit = {
  var connection: Connection = null
  try {
    Class.forName(h2Driver)
    val url = s"jdbc:h2:file:$target/test;"
    connection = DriverManager.getConnection(url, userName, password)
    val tableTypes = Array("TABLE")
    val rs = connection.getMetaData.getTables(null, null, "%", tableTypes)
    if (!rs.next()) {
      sys.error(s"No tables found. Expected $expectedTables tables to exist after Liquibase update.")
    } else {
      var actualTables: Seq[String] = Seq.empty[String]
      do {
        val tableName = rs.getString("TABLE_NAME")
        actualTables = actualTables :+ tableName
      } while (rs.next())

      if (expectedTables.diff(actualTables).nonEmpty || actualTables.diff(expectedTables).nonEmpty) {
        sys.error(s"Not the same number of tables. Expected tables: $expectedTables - Actual tables $actualTables")
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

scalaVersion := "2.12.6"
name := "sbt-liquibase-test-rollbackcount"
organization := "se.sambera"
version := "0.1.0"

libraryDependencies += "com.h2database" % "h2" % "1.4.197"

liquibaseUsername := userName
liquibasePassword := password
liquibaseDriver := h2Driver
liquibaseUrl := s"jdbc:h2:file:${target.value / "test"};INIT=CREATE SCHEMA IF NOT EXISTS TEST;"
liquibaseChangelog := "src/main/database/changelog-master.xml"

TaskKey[Unit]("checkTableExist") := {
  println("Checking state of the database after 'liquibaseUpdate'.")
  val expectedTables = Seq("LOCATION", "PERSON", "ADDRESS", "DATABASECHANGELOG", "DATABASECHANGELOGLOCK")

  checkContainsTables(expectedTables, target.value.absolutePath)
}

TaskKey[Unit]("checkTableAddressIsDrop") := {
  println("Checking state of the database after 'checkTableAddressIsDrop'.")
  val expectedTables = Seq("LOCATION", "PERSON", "DATABASECHANGELOG", "DATABASECHANGELOGLOCK")

  checkContainsTables(expectedTables, target.value.absolutePath)
}

TaskKey[Unit]("checkTablePersonIsDrop") := {
  println("Checking state of the database after 'checkTablePersonIsDrop'.")
  val expectedTables = Seq("LOCATION", "DATABASECHANGELOG", "DATABASECHANGELOGLOCK")

  checkContainsTables(expectedTables, target.value.absolutePath)
}
