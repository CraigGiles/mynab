package com.gilesc
package mynab
package testkit

abstract class DatabaseTestCase extends TestCase {
  private[this] def migrateDatabase(): Unit = {
    import org.flywaydb.core.Flyway

    case class DatabaseConfig(
      driver: String,
      url: String,
      username: String,
      password: String
    )

    val config = pureconfig.loadConfigOrThrow[DatabaseConfig]("mynab.database")
    val flyway = new Flyway()

    flyway.setLocations("classpath:db/migration")
    flyway.setDataSource(config.url, config.username, config.password)
    flyway.clean()
    flyway.migrate()

    ()
  }

  migrateDatabase()
}
