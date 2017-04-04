package com.gilesc
package mynab
package persistence
package account

import scalikejdbc._
import scalikejdbc.jsr310._
import java.time._
import com.gilesc.mynab.account._
import com.gilesc.commons.validation.ValidationError
import cats.implicits._

import com.gilesc.commons.validation._

sealed trait AccountGroupPersistenceError
case object DuplicateAccountGroupId extends AccountGroupPersistenceError
case class InvalidAccountNameLength(value: String) extends AccountGroupPersistenceError

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

  val read: AccountName => Either[AccountGroupPersistenceError, Option[AccountGroupRow]] = { n =>
    import cats._
    import cats.implicits._

    val opt = DB autoCommit { implicit session =>
      sql"""SELECT * FROM account_groups WHERE name=${n.value};"""
        .map(AccountGroupDAO.fromDb)
        .first()
        .apply()
    }

    // sequenceU will take Option[Either[A, B]] and turn it into
    // Either[A, Option[B]]. Then we leftMap to convert the left
    // side to the AccountGroupPersistenceError.
    opt.sequenceU leftMap {
      case InvalidLengthError(n) => InvalidAccountNameLength(n)
    }
  }

  val count: () => Int = { () =>
    DB autoCommit { implicit session =>
      sql"""SELECT COUNT(*) as count FROM account_groups;"""
        .map(rs => rs.int("count"))
        .single()
        .apply()
        .getOrElse(0)
    }
  }
}
