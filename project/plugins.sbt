resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "Flyway" at "https://flywaydb.org/repo"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.10")
addSbtPlugin("org.flywaydb" % "flyway-sbt" % "4.0.3")

// NOTE: backend/build.sbt has the same version of mysql-connector-java.
//       Please keep these two versions in sync.
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.36"
