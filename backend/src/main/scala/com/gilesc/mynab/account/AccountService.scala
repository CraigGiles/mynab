package com.gilesc.mynab
package account

import com.gilesc.commons.validation.ValidationError
import com.gilesc.mynab.logging.LoggingModule
import com.gilesc.mynab.repository._

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


