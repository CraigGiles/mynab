package com.gilesc.mynab
package account

import com.gilesc.commons.validation.ValidationError
import com.gilesc.mynab.logging.LoggingModule
import com.gilesc.mynab.repository._
import com.gilesc.mynab.transaction.Transaction

import cats._
import cats.implicits._

sealed trait AccountActionResult
final case class Success(account: Account) extends AccountActionResult
final case class Failure(message: String) extends AccountActionResult

case class AccountDetails(id: Long, accountName: String, accountType: String, groupName: String)
case class FindDetails(name: String)

trait AccountServiceModule {
  def create: AccountDetails => AccountActionResult
  def find: FindDetails => Option[Account]
}

object AccountService {

  def create(save: Account => PersistenceResult,
    log: LoggingModule)(details: AccountDetails): AccountActionResult = {

    val results = convert(details) map { account =>
      save(account) match {
        case PersistenceSuccessful =>
          Success(account)

        case x =>
          log.info(s"Persistence Failure: $x")
          Failure(x.toString)
      }
    }

    results match {
      case Left(s) => Failure(s)
      case Right(a) => a
    }
  }

  def find(account: FindBy => Option[Account],
    log: LoggingModule)(find: FindDetails): Option[Account] = {

    log.info(s"Finding account by name: ${find.name}")
    val either: Either[ValidationError, Option[Account]] = for {
      n <- AccountName(find.name)
    } yield account(FindByName(n))

    either.toOption.flatten
  }

  val convert: AccountDetails => Either[String, Account] = { details =>
    val i = AccountId(details.id)
    val either = for {
      t <- AccountType(details.accountType)
      n <- AccountName(details.accountName)
      g <- AccountName(details.groupName)
    } yield Account.create(i, t, n)

    either.left.map(_.toString)
  }
}

// ---------------------------------------------------------------------------
// NEW DOMAIN
// ---------------------------------------------------------------------------
final case class DuplicateIdException() extends Exception("Duplicate ID")

sealed trait PersistenceFailure
final case object DuplicateIdError extends PersistenceFailure
final case class InvalidAccountGroupId(id: AccountGroupId) extends PersistenceFailure

trait NewAccountGroupService {
  def createGroup: AccountName => Either[String, AccountGroup]
}

object NewAccountGroupService {
  def create(save: AccountName => Either[PersistenceFailure, AccountGroupId])
    (name: AccountName): Either[String, AccountGroup] = {

    save(name) match {
      case Right(id) => Right(AccountGroup.create(id, name))
      case Left(DuplicateIdError) => Left(DuplicateIdError.toString)
      case Left(InvalidAccountGroupId(id)) => Left(s"$id exists") // TODO: remove
    }
  }
}

case class AccountContext(group: AccountGroupId, name: AccountName, accType: AccountType)

object NewAccountService {

  def create(
    save: AccountContext => Either[PersistenceFailure, AccountId],
    find: AccountGroupId => Option[AccountGroup])
    (ctx: AccountContext): Either[PersistenceFailure, AccountGroup] = {

    val eitherAccountGroup = Either.fromOption(find(ctx.group) , InvalidAccountGroupId(ctx.group))
    val eitherAccountId: Either[PersistenceFailure, AccountId] = save(ctx)

    for {
      group <- eitherAccountGroup
      accId <- eitherAccountId
    } yield group.copy(accounts = group.accounts :+ Account.create(accId, ctx.accType, ctx.name))
  }
}
