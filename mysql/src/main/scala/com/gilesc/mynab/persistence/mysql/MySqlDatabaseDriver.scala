package com.gilesc
package mynab
package persistence
package mysql

import slick.jdbc.MySQLProfile

/**
  * A mysql driver with extended support.
  */
trait MySqlDatabaseDriver extends MySQLProfile {

  object MyAPI extends API {
    // TODO: any specific overrides that i would need to do, do them here
  }

  override val api = MyAPI
}

object MySqlDatabaseDriver extends MySqlDatabaseDriver
