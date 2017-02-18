package com.gilesc.mynab
package account

import cats.Monad
import cats.data.{Kleisli, ReaderT}

import scala.concurrent.Future

case class AccountDetails(accountName: String, accountType: String, groupName: String)

sealed trait AddAccountResult
final case class Success(account: Account) extends AddAccountResult
final case class Failure(message: String) extends AddAccountResult

sealed trait PersistenceResult
case object PersistenceSuccessful extends PersistenceResult
case class PersistenceFailure(message: String) extends PersistenceResult

case class Config()

trait AccountServiceModule {
  def create: AccountDetails => AddAccountResult
}

trait AccountRepositoryModule {
  def save: Account => PersistenceResult
}

trait LoggingModule {
  def log: String => Unit
}

object InMemoryAccountRepository extends AccountRepositoryModule {
  def save: Account => PersistenceResult = { account =>
    println(s"Persisting $account")
    PersistenceSuccessful
  }
}

object PrintlnLoggingService extends LoggingModule {
  def log: String => Unit = println
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

object Main extends App with AccountServiceModule {
  val details = AccountDetails("My Account Name", "Banking", "No Group Name")

  def create = AccountService.create(InMemoryAccountRepository.save, PrintlnLoggingService.log)
  val account = create(details)

  println(account)
}
