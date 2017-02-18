package com.gilesc.mynab
package account

import com.gilesc.mynab.logging.LoggingModule
import com.gilesc.mynab.repository._

sealed trait AddAccountResult
final case class Success(account: Account) extends AddAccountResult
final case class Failure(message: String) extends AddAccountResult

case class AccountDetails(accountName: String, accountType: String, groupName: String)

trait AccountServiceModule {
  def create: AccountDetails => AddAccountResult
}

object AccountService {

  def create(accounts: AccountRepositoryModule,
    log: LoggingModule)(details: AccountDetails): AddAccountResult = {

    val results = convert(details) map { account =>
      accounts.save(account) match {
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

  val convert: AccountDetails => Either[String, Account] = { details =>
    val either = for {
      t <- AccountType(details.accountType)
      n <- AccountName(details.accountName)
      g <- AccountName(details.groupName)
    } yield Account.create(t, n)

    either.left.map(_.toString)
  }
}


