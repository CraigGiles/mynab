package com.gilesc
package mynab
package persistence
package mysql

import java.util.Properties

import com.gilesc.mynab.persistence.DatabaseProfile
import com.typesafe.config.Config
import slick.jdbc.JdbcProfile
import slick.jdbc.H2Profile
import slick.jdbc.MySQLProfile

import scala.concurrent.{ExecutionContext, Future}

object SlickDatabaseProfile {
  def apply(config: Config)(implicit ec: ExecutionContext): SlickDatabaseProfile = {
    val profile = config.getString("database.profile") match {
      case "mysql" => MySQLProfile
       case _ => H2Profile
    }

    new SlickDatabaseProfile(profile, config)
  }
}

class SlickDatabaseProfile(val profile: JdbcProfile, config: Config)
    (implicit val ec: ExecutionContext) extends DatabaseProfile {

  import profile.api._
  val properties = new Properties()
  properties.setProperty("connectionPool", "disabled")
  properties.setProperty("user", config.getString("database.user"))
  properties.setProperty("password", config.getString("database.password"))
  properties.setProperty("keepAliveConnection", "true")

  val dbConfig: profile.backend.DatabaseDef = profile.backend.Database.forURL(
    url = config.getString("database.url"),
    driver = "org.h2.Driver", //TODO config
    prop = properties
  )

  // val db = Database.forConfig("database")

  def execute[T](action: DBIO[T]): Future[T] = dbConfig.run(action)
}
