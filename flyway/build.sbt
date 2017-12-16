// Database Migrations:
// run with "sbt flywayMigrate"
// http://flywaydb.org/getstarted/firststeps/sbt.html
lazy val databaseUrl = 
  sys.env.getOrElse("DB_DEFAULT_URL", "jdbc:mysql://localhost:3306/dev")
lazy val databaseUser = sys.env.getOrElse("DB_DEFAULT_USER", "root")
lazy val databasePassword = sys.env.getOrElse("DB_DEFAULT_PASSWORD", "root")

flywayLocations := Seq("classpath:db/migration")
flywayUrl := databaseUrl
flywayUser := databaseUser
flywayPassword := databasePassword

// import org.flywaydb.sbt.FlywayPlugin

// lazy val CustomConfig =
//   config("custom") describedAs "A custom config." extend Runtime

// lazy val customSettings: Seq[Def.Setting[_]] = Seq(
//   flywayUser := databaseUser,
//   flywayPassword := databasePassword,
//   flywayUrl := databaseUrl,
// )

// lazy val app = (project in file("app")).
//   settings(inConfig(CustomConfig)(FlywayPlugin.flywayBaseSettings(CustomConfig) ++ customSettings): _*)
