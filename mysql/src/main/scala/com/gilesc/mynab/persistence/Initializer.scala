package com.gilesc
package mynab
package persistence

import scalikejdbc.config.DBs
import scalikejdbc._

object Initializer {

  def init() = {
    DBs.setupAll()
    ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")
  }

  def stop() = {
    DBs.closeAll()
  }
}
