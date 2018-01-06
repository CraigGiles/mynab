package com.gilesc
package mynab

import com.gilesc.mynab.testkit.TestCase

abstract class InMemoryDatabase extends TestCase {

  private[this] def migrateDatabase(): Unit = {
    import org.flywaydb.core.Flyway
    val flyway = new Flyway()
    lazy val databaseUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    lazy val databaseUser = "sa"
    lazy val databasePassword = ""

    flyway.setLocations("classpath:db/migration")

    flyway.setDataSource(databaseUrl, databaseUser, databasePassword)

    flyway.migrate()

    ()
  }
  migrateDatabase()
}
