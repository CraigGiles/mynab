package com.gilesc
package mynab
package repository
package mysql

import doobie.enum.SqlState

sealed abstract class ErrorCode(val code: SqlState, val reason: String)

object ErrorCode {
  case object DuplicateKey extends ErrorCode(SqlState("23000"), "Can't write; duplicate key in table")
  case object BadHostError extends ErrorCode(SqlState("08S01"), "Message: Can't get hostname for your address")
  case class UnknownError(override val code: SqlState) extends ErrorCode(code, "Unknown Error")

  def apply(code: SqlState): ErrorCode = code match {
    case DuplicateKey.code | SqlState("23505") => DuplicateKey
    case BadHostError.code => BadHostError
    case e => UnknownError(e)
  }

  /**
   * Partial Function for converting SqlState to internal mynab ErrorCode
   * There are probably better ways to handle the vendor specific error codes
   * that doobie spits out, but this seemed like a good quick and dirty
   * solution while keeping in mind that the only other error codes
   * i'll be handling are the h2 codes for the test database.
   */
  def convert = new PartialFunction[doobie.SqlState, ErrorCode] {
    override def isDefinedAt(x: doobie.SqlState) = true
    override def apply(state: doobie.SqlState) = ErrorCode.apply(state)
  }
}
