package se.sambera.sbt.plugins

import sbt.Keys.Classpath
import sbt.internal.inc.classpath.ClasspathUtilities

object ClassLoader {
  def getClassLoader(classpath: Classpath): ClassLoader = {
    ClasspathUtilities.toLoader(classpath.map(_.data))
  }
}
