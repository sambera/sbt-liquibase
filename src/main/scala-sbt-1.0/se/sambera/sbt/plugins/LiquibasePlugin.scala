
package se.sambera.sbt.plugins

import liquibase.Liquibase
import liquibase.database.Database
import liquibase.integration.commandline.CommandLineUtils
import liquibase.resource.{ClassLoaderResourceAccessor, FileSystemResourceAccessor}
import sbt.Keys.dependencyClasspath
import sbt.complete.DefaultParsers.{IntBasic, Space, token}
import sbt.{Configuration, Def, _}
import se.sambera.utils.FileConsoleOutputStreamWriter

object LiquibasePlugin extends AutoPlugin {
  // by defining autoImport, the settings are automatically imported into user's `*.sbt`
  object Import {
    // configuration points
    val liquibaseUpdate             = TaskKey[Unit]("liquibase-update", "Run a liquibase migration")
    val liquibaseValidateChangelog  = TaskKey[Unit]("liquibase-validate-changelog", "Checks changelogs for bad MD5Sums and preconditions")
    val liquibaseStatus             = TaskKey[Unit]("liquibase-status", "Print count of unrun change sets")
    val liquibaseDropAll           = TaskKey[Unit]("liquibase-drop-all", "Drop all database objects owned by user")
    val liquibaseRollbackCount     = InputKey[Unit]("liquibase-rollback-count", "<num>Rolls back the last <num> change sets applied to the database")

    val liquibaseUrl                = SettingKey[String]("liquibase-url", "The url for liquibase")
    val liquibaseUsername           = SettingKey[String]("liquibase-username", "username")
    val liquibasePassword           = SettingKey[String]("liquibase-password", "password")
    val liquibaseDriver             = SettingKey[String]("liquibase-driver", "driver")
    val liquibaseChangelog            = SettingKey[String]("liquibase-changelog", "This is your liquibase changelog file to run.")

    val liquibaseDefaultCatalog     = SettingKey[Option[String]]("liquibase-default-catalog", "default catalog")
    val liquibaseDefaultSchemaName  = SettingKey[Option[String]]("liquibase-default-schema-name", "default schema name")

    val liquibaseChangelogCatalog     = SettingKey[Option[String]]("liquibase-changelog-catalog", "changelog catalog")
    val liquibaseChangelogSchemaName  = SettingKey[Option[String]]("liquibase-changelog-schema-name", "changelog schema name")

    val liquibaseOutputDefaultCatalog = SettingKey[Boolean]("liquibase-output-default-catalog", "Whether to ignore the catalog name.")
    val liquibaseOutputDefaultSchema  = SettingKey[Boolean]("liquibase-output-default-schema", "Whether to ignore the schema name.")

    val liquibaseContext              = SettingKey[String]("liquibase-context","changeSet contexts to execute")

    lazy val database                 = TaskKey[Database]("liquibase-database", "the database")
    lazy val liquibaseInstance        = TaskKey[Liquibase]("liquibase", "liquibase object")
  }

  import Import._

  val autoImport: Import.type = Import

  override def requires = sbt.plugins.JvmPlugin
  // This plugin is automatically enabled for projects which are JvmPlugin.
  override def trigger = allRequirements

  // a group of settings that are automatically added to projects.
  override val projectSettings: Seq[Def.Setting[_]] =  inConfig(Compile)(liquibaseBaseSettings(Compile)) ++ inConfig(Test)(liquibaseBaseSettings(Test))

  def liquibaseBaseSettings(conf: Configuration) : Seq[Setting[_]] = {

    Seq[Setting[_]](
      liquibaseDefaultCatalog := None,
      liquibaseChangelogCatalog := None,
      liquibaseDefaultSchemaName := None,
      liquibaseChangelogSchemaName := None,
      liquibaseContext := "",
      liquibaseOutputDefaultCatalog := true,
      liquibaseOutputDefaultSchema := true,

      database :=
        Def.taskDyn {
          val classpath = (dependencyClasspath in conf).value
          Def.task {
            val accessor = new ClassLoaderResourceAccessor(sbt.internal.inc.classpath.ClasspathUtilities.toLoader(classpath.map(_.data)))
            CommandLineUtils.createDatabaseObject(
              accessor,
              liquibaseUrl.value,
              liquibaseUsername.value,
              liquibasePassword.value,
              liquibaseDriver.value,
              liquibaseDefaultCatalog.value.orNull,
              liquibaseDefaultSchemaName.value.orNull,
              false, // outputDefaultCatalog
              true, // outputDefaultSchema
              null, // databaseClass
              null, // driverPropertiesFile
              null, // propertyProviderClass
              liquibaseChangelogCatalog.value.orNull,
              liquibaseChangelogSchemaName.value.orNull,
              null, // databaseChangeLogTableName
              null // databaseChangeLogLockTableName
            )
          }
        }.value,


      liquibaseInstance := {
        new Liquibase(liquibaseChangelog.value, new FileSystemResourceAccessor, database.value)
      },

      liquibaseUpdate := liquibaseInstance.value.execAndClose(_.update(liquibaseContext.value)),

      liquibaseValidateChangelog := liquibaseInstance.value.execAndClose(_.validate()),

      liquibaseStatus := liquibaseInstance.value.execAndClose(_.reportStatus(true, liquibaseContext.value, new FileConsoleOutputStreamWriter("target/unRunChangeSets.txt", System.out))),

      liquibaseDropAll := liquibaseInstance.value.execAndClose(_.dropAll()),

      liquibaseRollbackCount := {
          val count = token(Space ~> IntBasic, "<count>").parsed
          liquibaseInstance.value.execAndClose(_.rollback(count, liquibaseContext.value))
      }
    )
  }

  implicit class RichLiquibase(val liquibase: Liquibase) extends AnyVal {
    def execAndClose(f: Liquibase => Unit): Unit = {
      try { f(liquibase) } finally { liquibase.getDatabase.close() }
    }
  }
}
