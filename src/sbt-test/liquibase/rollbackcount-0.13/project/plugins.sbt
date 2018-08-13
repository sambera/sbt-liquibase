sys.props.get("plugin.version") match {
  case Some(pluginVersion) => addSbtPlugin("se.sambera" % "sbt-liquibase" % pluginVersion)
  case _ => sys.error("""|The system property 'plugin.version' is not defined.
                         |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

val h2database    = "com.h2database" % "h2" % "1.4.197"
val liquibaseCore = "org.liquibase" % "liquibase-core" % "3.6.1"

libraryDependencies ++= Seq(h2database, liquibaseCore)
