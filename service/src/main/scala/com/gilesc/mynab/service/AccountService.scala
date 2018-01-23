package com.gilesc
package mynab
package service

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import cats.implicits._
import com.gilesc.arrow.Service
import java.util.UUID


sealed abstract class PersistError(
  msg: String,
  err: Throwable = null
) extends Exception(msg, err)

object PersistResponse {
  def success[T](value: T): Either[PersistError, T] = Right(value)
  def error[T](error: PersistError): Either[PersistError, T] = Left(error)
}

object PersistError {
  final case class DuplicateKeyError(msg: String) extends PersistError(msg)
  final case object InvalidResponseError extends PersistError("Obtained response that shouldn't have obtained")
}

case class CreateAccountContext(userId: UserId, name: AccountName)

final class CreateAccountService(
    persist: Service[Future, Account, Either[PersistError, Account]]
  ) extends Service[Future, CreateAccountContext, Account] {
  override def run(request: CreateAccountContext): Future[Account] = {
    val account = Account(
      AccountId(UUID.randomUUID()),
      request.userId,
      request.name,
      Vector.empty[Transaction])

    persist(account) flatMap {
      case Right(acc) => Future.successful(acc)
      case Left(error) => Future.failed(error)
    }
  }
}

final class ReadAccountService(
    repository: Service[Future, UserId, Either[PersistError, Option[Vector[Account]]]]
  ) extends Service[Future, UserId, Vector[Account]] {

  override def run(id: UserId): Future[Vector[Account]] = {
    repository(id) flatMap {
      case Right(Some(accounts)) => Future.successful(accounts)
      case Right(None) => Future.successful(Vector.empty[Account])
      case Left(error) => Future.failed(error)
    }
  }
}

abstract class UpdateError(msg: String) extends Exception(msg)
case class NoAccountFound(id: AccountId) extends UpdateError(s"No account found for accountId: $id")
case class UnknownError(id: AccountId) extends UpdateError(s"Unknown error for updating $id")

final class UpdateAccountNameService(
  lookup: Service[Future, AccountId, Either[PersistError, Option[Account]]],
  persist: Service[Future, Account, Either[PersistError, Account]]
  ) extends Service[Future, (AccountId, AccountName), Either[UpdateError, Account]] {

  override def run(req: (AccountId, AccountName)): Future[Either[UpdateError, Account]] = {
    val (id, replacement) = req

    lookup(id) flatMap {
      case Right(Some(account)) => update(account, replacement, persist)
      case Right(None) => Future.successful(Left(NoAccountFound(id)))
      case Left(error) => Future.failed(error)
    }
  }

  def update(
    account: Account,
    replacement: AccountName,
    persist: Service[Future, Account, Either[PersistError, Account]]
  ): Future[Either[UpdateError, Account]] = {
    persist(account.copy(name = replacement)) flatMap {
      case Right(acc) => Future.successful(Right(acc))
      case Left(error) => Future.failed(error)
    }
  }
}

// final class CreateTransactionService(
//   lookup: Service[AccountId, Either[MysqlError, Account]],
//   persist: Service[Account, Either[MysqlError, Account]]
// ) extends Service[(AccountId, TransactionContext), Either[UpdateError, Account]] {
//   def apply(request: (AccountId, TransactionContext)): Future[Either[UpdateError, Account]] = {
//     val (id, ctx) = request
//     lookup(id) flatMap {
//       case Right(Success(account)) => update(account, ctx, persist)
//       case Right(NotFound()) => Future(Left(NoAccountFound(id)))
//       case Left(error) => Future.failed(error)
//     }
//   }

//   def update(
//     account: Account,
//     ctx: TransactionContext,
//     persist: Service[Account, Either[MysqlError, Account]],
//     persistT: Service[Transaction, Either[MysqlError, Transaction]]
//   ): Future[Either[UpdateError, Account]] = {
//     val transaction = persistT flatMap {
//       case Right(Success(t)) => Future(t)
//       case Left(error) => Future.failed(error)
//       case _ => Future.failed(UnknownError)
//     }

//     val newaccount = account.transactions :+ transaction
//   }
// }

final class AddTransactionAccountService(
  lookup: Service[Future, AccountId, Either[PersistError, Option[Account]]],
  persist: Service[Future, (Account, TransactionContext), Either[PersistError, Account]]
) extends Service[Future, (AccountId, TransactionContext), Account] {

  override def run(request: (AccountId, TransactionContext)): Future[Account] = {
    val (id, ctx) = request

    val futRespAcct = lookup(id) flatMap {
      case Right(Some(acct)) => persist((acct, ctx))
      case Left(error) => Future.failed(error)
      case _ => Future.failed(UnknownError(id))
    }

    futRespAcct flatMap {
      case Right(acct) => Future.successful(acct)
      case Left(error) => Future.failed(error)
      case _ => Future.failed(UnknownError(id))
    }
  }
}
