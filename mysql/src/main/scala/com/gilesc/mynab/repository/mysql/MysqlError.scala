package com.gilesc
package mynab
package repository
package mysql

import com.mysql.jdbc.exceptions.jdbc4._
import java.sql.SQLException

sealed abstract class MysqlError(val contains: String, val repositoryError: RepositoryError)

object MysqlError {
  case object DuplicateKey extends MysqlError("Duplicate entry", RepositoryError.DuplicateKey)
  case object ForeignKeyConstraint extends MysqlError("foreign key constraint fails", RepositoryError.ForeignKeyConstraint)
  case class UnknownError(
    override val contains: String,
    msg: String,
    sqlState: String,
    throwable: Throwable
  ) extends MysqlError(contains, RepositoryError.UnknownError(msg, sqlState, throwable))

  def convert: SQLException => MysqlError = {
    case ex: MySQLIntegrityConstraintViolationException if ex.getLocalizedMessage.contains(DuplicateKey.contains) =>
      MysqlError.DuplicateKey
    case ex: MySQLIntegrityConstraintViolationException if ex.getLocalizedMessage.contains(ForeignKeyConstraint.contains) =>
      MysqlError.ForeignKeyConstraint
    case ex => MysqlError.UnknownError("", ex.getLocalizedMessage, ex.getSQLState, ex)
  }

}

