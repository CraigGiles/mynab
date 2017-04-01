package com.gilesc
package mynab
package persistence

import scalikejdbc._
import scalikejdbc.config._
import com.typesafe.config.ConfigFactory

object DatabaseInitializer {

  def init() = {
    val config = ConfigFactory.load()
    val env = config.getString("db.env")

    DBsWithEnv(env).setupAll()
  }

  def stop() = {
    DBs.closeAll()
  }
}
