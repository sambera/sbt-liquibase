Liquibase plugin for sbt 1.1.5
====================================
Plugin that applies database changes using Liquibase.

# Latest version
2018-05-30  version 0.1.0

# Instructions for use:
### Step 1: Include the plugin in your build

Add the following to your `project/plugins.sbt`:

    // Adds sambera-release resolver
    resolvers += "sambera-github" at "https://raw.githubusercontent.com/sambera/releases/master/"

    // Enable liquibase updates
    addSbtPlugin("se.sambera" % "sbt-liquibase" % "0.1.0")


### Step 2: Activate sbt-liquibase in your build

Add the following to your 'build.sbt' ( if you are using build.sbt )


    
    import se.sambera.LiquibasePlugin
    
    lazy val test = (project in file("."))
      .enablePlugins(LiquibasePlugin)
      .settings(
         liquibaseUsername  := "<database_username>",
         liquibasePassword  := "<database_user_password>",
         liquibaseDriver    := "<database_driver>",
         liquibaseUrl       := "<database_url>",
         liquibaseChangelog := "<changelog_pathname_file>",
         
         // rest of your setting configuration
         )


## Settings

|Setting|Description|Example|
|-------|-----------|-------|
|liquibaseUsername|Username for the database. This defaults to blank. Required.|`samberauser`|
|liquibasePassword|Password for the database. This defaults to blank. Required.|`secretpassword123`|
|liqubaseDriver|Database driver classname. There is no default. Required.|`com.mysql.jdbc.Driver`|
|liquibaseUrl|Database connection uri. There is no default. Required.|`jdbc:mysql://localhost:3306/mydb`|
|liquibaseChangelog|Full path to your changelog file. This defaults to blank. Required.|`src/main/database/changelog-master.xml"`|
|liquibaseChangelogCatalog|Default catalog name for the changelog tables. This defaults to None. Optional.|`Some("my_catalog")`|
|liquibaseChangelogSchemaName|Default schema name for the changelog tables. This defaults to None. Optional.|`Some("my_schema")`|
|liquibaseDefaultCatalog|Default catalog name for the db if it isn't defined in the uri. This defaults to None. Optional.|`Some("my_catalog")`|
|liquibaseDefaultSchemaName|Default schema name for the db if it isn't defined in the uri. This defaults to None. Optional.|`Some("my_schema")`|
|liquibaseOutputDefaultCatalog|Whether to ignore the catalog name. This defaults to true. ||
|liquibaseOutputDefaultSchema|Whether to ignore the schema name. This defaults to true. ||

## Tasks

|Task|Description|
|----|-----------|
|`liquibaseUpdate`|Run the liquibase migration|
|`liquibaseValidateChangelog`|Checks changelogs for bad MD5Sums and preconditions.|
|`liquibaseStatus`|Print count of yet to be run changesets|
|`liquibaseDropAll`|Drop all tables|
|`liquibaseRollbackCount {int}`|Rolls back the last {int i} change sets applied to the database|

Notes
------------------
We have been using bigtoast/'sbt-liquibase' for a while but no updates has been done for the last five years (last commit 23/12/2013).

We needed a liquibase-plugin that support the latest sbt version so we use 'bigtoast' and 'sbtliquibase' as a source
of inspiration to create this plugin.


Acknoledgements
---------------
I used the following plugins as reference

 * bigtoast/sbt-liquibase (https://github.com/bigtoast/sbt-liquibase)
 * sbtliquibase/sbt-liquibase-plugin (https://github.com/sbtliquibase/sbt-liquibase-plugin)

