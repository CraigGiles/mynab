package com.gilesc
package mynab
package persistence
package account

// save: AccountName => Either[AccountGroupPersistenceError, AccountGroupId]

import scalikejdbc._
import scalikejdbc.jsr310._
import java.time._
import com.gilesc.mynab.account._
import com.gilesc.commons.validation.ValidationError
import cats.implicits._

sealed trait AccountGroupPersistenceError
case object DuplicateAccountGroupId extends AccountGroupPersistenceError

case class AccountGroupRow(id: AccountGroupId, name: AccountName, createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime, deletedAt: Option[ZonedDateTime])

object AccountGroupDAO {
  def fromDb(rs: WrappedResultSet): Either[ValidationError, AccountGroupRow] = {
    val id = AccountGroupId(rs.long("id"))
    val name = AccountName(rs.string("name"))
    val created = rs.zonedDateTime("created_at")
    val updated = rs.zonedDateTime("updated_at")
    val deleted = rs.zonedDateTimeOpt("deleted_at")

    name map(AccountGroupRow(id, _, created, updated, deleted))
  }
}

object AccountGroupRepository {
  DatabaseInitializer.init()
  implicit val session = AutoSession

  type PersistenceResult[T] = Either[AccountGroupPersistenceError, T]

  val create: AccountName => PersistenceResult[AccountGroupId] = { n => 
    DB autoCommit { implicit session =>
      val id = sql"""
      |INSERT INTO account_groups (name)
      |VALUES
      |  (${n.value});
      """.stripMargin('|').updateAndReturnGeneratedKey.apply()

      Right(AccountGroupId(id))
    }
  }
}
