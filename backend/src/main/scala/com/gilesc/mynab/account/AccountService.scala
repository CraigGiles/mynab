package com.gilesc.mynab.account

import com.gilesc.mynab.repository._

sealed trait AddAccountResult
final case class Success(account: Account) extends AddAccountResult
final case class Failure(message: String) extends AddAccountResult

case class AccountDetails(accountName: String, accountType: String, groupName: String)

trait AccountServiceModule {
  def create: AccountDetails => AddAccountResult
}

object AccountService {

  def create(persist: Account => PersistenceResult,
             log: String => Unit)(details: AccountDetails): AddAccountResult = {

    val accountE = for {
      t <- AccountType(details.accountType)
      n <- AccountName(details.accountName)
      g <- AccountName(details.groupName)
    } yield Account.create(t, n)

    accountE match {
      case Left(s) =>
        Failure(s.toString)

      case Right(a) =>
        persist(a) match {
          case PersistenceSuccessful =>
            Success(a)

          case x =>
            log(s"Persistence Failure: $x")
            Failure(x.toString)
        }
    }
  }
}


