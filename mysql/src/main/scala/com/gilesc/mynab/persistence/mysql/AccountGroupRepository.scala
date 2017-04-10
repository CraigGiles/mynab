package com.gilesc
package mynab
package persistence
package account

import java.time._
import com.gilesc.mynab.persistence.mysql._
import com.gilesc.mynab.account._
import com.gilesc.mynab.user.UserId
import com.gilesc.commons.validation.ValidationError
import cats.implicits._

import com.gilesc.commons.validation._

import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global

sealed trait AccountGroupPersistenceError
case object DuplicateAccountGroupId extends AccountGroupPersistenceError
case class InvalidAccountNameLength(value: String) extends AccountGroupPersistenceError

case class AccountGroupSlick(id: Long, userId: Long, name: String, createdAt: OffsetDateTime,
  updatedAt: OffsetDateTime, deletedAt: Option[OffsetDateTime])
case class AccountGroupRow(id: AccountGroupId, name: AccountName, createdAt: OffsetDateTime,
  updatedAt: OffsetDateTime, deletedAt: Option[OffsetDateTime])

import slick.jdbc.H2Profile.api._// TODO: REmove

class AccountGroupTable(tag: Tag) extends Table[AccountGroupSlick](tag, "account_groups") {
  import java.sql.Date
  import java.time._
  implicit val offsetDateTime = MappedColumnType.base[OffsetDateTime, String](
    z => z.toString,
    d => OffsetDateTime.parse(d)
  )

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def userId = column[Long]("user_id")
  def name = column[String]("name")
  def created_at = column[OffsetDateTime]("created_at", O.Default(OffsetDateTime.now))
  def updated_at = column[OffsetDateTime]("updated_at", O.Default(OffsetDateTime.now))
  def deleted_at = column[Option[OffsetDateTime]]("deleted_at")

  def * = (id, userId, name, created_at, updated_at, deleted_at) <> (AccountGroupSlick.tupled, AccountGroupSlick.unapply _)
}


object AccountGroupRepository {
  import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
  type DuplicateKey = MySQLIntegrityConstraintViolationException
  type PersistenceResult[T] = Either[AccountGroupPersistenceError, T]
  case class CreateContext(userId: UserId, name: AccountName)

  lazy val AccountGroupTable = TableQuery[AccountGroupTable]

  val createUsingDatabase: SlickDatabaseProfile => CreateContext => PersistenceResult[AccountGroupId] = { db => ctx =>
      import db._
      import db.profile.api._

      val insertQuery = AccountGroupTable returning AccountGroupTable.map(_.id) into ((ag, id) => ag.copy(id = id))
      val ts = OffsetDateTime.now()
      val action = insertQuery += AccountGroupSlick(0L,
        ctx.userId.value,
        ctx.name.value,
        ts, ts, None)

      val v = db.execute(action.asTry).map {
        case scala.util.Success(r) => Right(AccountGroupId(r.id))

        case scala.util.Failure(e) => e match {
          case ex: DuplicateKey if ex.getMessage.contains("username") =>
            Left(InvalidAccountNameLength(ctx.name.value))

          case ex =>
            Left(InvalidAccountNameLength(ex.toString))
        }
      }

      scala.concurrent.Await.result(v, scala.concurrent.duration.Duration.Inf)
    }

  val create: CreateContext => PersistenceResult[AccountGroupId] =
    createUsingDatabase(SlickDatabaseProfile.apply(ConfigFactory.load()))


  val read: AccountName => Either[AccountGroupPersistenceError, Option[AccountGroupRow]] = { n =>
    import cats._
    import cats.implicits._

    import scala.concurrent.ExecutionContext.Implicits.global
    val config = ConfigFactory.load()
    val database = SlickDatabaseProfile(config)
    import database._
    import database.profile.api._

    val query = AccountGroupTable.filter(_.name === n.value).result

    val v = database.execute(query)
    val result = scala.concurrent.Await.result(v, scala.concurrent.duration.Duration.Inf)
    def toRow(a: AccountGroupSlick): Either[ValidationError, AccountGroupRow] = {
      AccountName(a.name) map { name =>
        AccountGroupRow(
          AccountGroupId(a.id),
          name,
          a.createdAt,
          a.updatedAt,
          a.deletedAt)
      }
    }

    val results = result map toRow

    val opt: Option[Either[ValidationError, AccountGroupRow]] = results.headOption
//     val opt = DB autoCommit { implicit session =>
//       sql"""SELECT * FROM account_groups WHERE name=${n.value};"""
//         .map(AccountGroupDAO.fromDb)
//         .first()
//         .apply()
//     }

    // sequenceU will take Option[Either[A, B]] and turn it into
    // Either[A, Option[B]]. Then we leftMap to convert the left
    // side to the AccountGroupPersistenceError.
    opt.sequenceU leftMap {
      case InvalidLengthError(n) => InvalidAccountNameLength(n)
    }
  }

  val count: () => Int = { () =>
//     DB autoCommit { implicit session =>
//       sql"""SELECT COUNT(*) as count FROM account_groups;"""
//         .map(rs => rs.int("count"))
//         .single()
//         .apply()
//         .getOrElse(0)
//     }

    import scala.concurrent.ExecutionContext.Implicits.global
    import com.typesafe.config.ConfigFactory
    val config = ConfigFactory.load()
    val database = SlickDatabaseProfile(config)
    import database._
    import database.profile.api._
    val v = database.execute(AccountGroupTable.length.result)
    scala.concurrent.Await.result(v, scala.concurrent.duration.Duration.Inf)
//    AccountGroupTable.length
  }
}
