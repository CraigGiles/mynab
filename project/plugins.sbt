resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "Flyway" at "https://flywaydb.org/repo"

addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.2.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")

// ScalaJS Development: Taken from the template
addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.5")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.21")

// addSbtPlugin("com.lihaoyi" % "workbench" % "0.3.1")

// NOTE: backend/build.sbt has the same version of mysql-connector-java.
//       Please keep these two versions in sync.
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.36"
