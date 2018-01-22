package com.gilesc
package mynab
package service

import java.util.UUID

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import com.gilesc.arrow.Service

import com.gilesc.mynab.testkit.TestCase

import cats.implicits._

class AccountServiceSpec extends TestCase {

  behavior of "Account Service"
  it should "allow me to create an account given a valid name and user id" in {
     val repository = new Service[Future, Account, Either[PersistError, Account]] {
       def apply(a: Account): Future[Either[PersistError, Account]] = {
         Future(PersistResponse.success(a))
       }
     }

    val userId = UserId(UUID.randomUUID)
    val name = AccountName("mynab_account")
    val ctx = CreateAccountContext(userId, name)
    val create = new CreateAccountService(repository)

    val result = Await.result(create(ctx), Duration.Inf)
    result.userId should be(userId)
    result.name should be(name)
    result.transactions should be(Vector.empty[Transaction])
  }

  it should "allow me to find all accounts for a user in the database" in {
    val userId = generateUserId.head
    val generated = generateAccount(userId).take(5).toVector
    val repository = new Service[Future, UserId, Either[PersistError, Option[Vector[Account]]]] {
       def apply(u: UserId): Future[Either[PersistError, Option[Vector[Account]]]] = {
         Future.successful(PersistResponse.success(Some(generated)))
       }
     }

    val service = new ReadAccountService(repository)
    val Right(Some(accounts)) = Await.result(repository(userId), Duration.Inf)
    accounts should be(generated)
    accounts.foreach { _.userId should be(userId) }

    val result = Await.result(service(userId), Duration.Inf)
    result should be(generated)
    result.foreach { _.userId should be(userId) }
  }

  it should "allow me to update the account name of an account" in {
    val userId = generateUserId.head
    val generatedId = generateAccountId.head
    val generated = generateAccount(userId).head.copy(id = generatedId)

    val lookup = new Service[Future, AccountId, Either[PersistError, Option[Account]]] {
       def apply(id: AccountId): Future[Either[PersistError, Option[Account]]] = {
         Future.successful(PersistResponse.success(Some(generated)))
       }
     }

    val persist = new Service[Future, Account, Either[PersistError, Account]] {
       def apply(account: Account): Future[Either[PersistError, Account]] = {
         Future.successful(PersistResponse.success(account))
       }
     }

    val newname = generateAccountName.head
    val args = (generatedId, newname)
    val service = new UpdateAccountNameService(lookup, persist)
    val Right(result) = Await.result(service(args), Duration.Inf)
    result.name should be(newname)
  }

  it should "allow me to add a transaction to the account" in {
    val userId = generateUserId.head
    val transaction = generateTransaction.head
    val transactionId = transaction.id
    val transactionCtx = TransactionContext(
      transaction.date,
      transaction.payee,
      transaction.category,
      transaction.memo,
      transaction.withdrawal,
      transaction.deposit,
      transaction.cleared)
    val generatedId = generateAccountId.head
    val generated = generateAccount(userId).head.copy(
      id = generatedId,
      transactions = Vector.empty[Transaction]
    )

    val lookup = new Service[Future, AccountId, Either[PersistError, Option[Account]]] {
       def apply(id: AccountId): Future[Either[PersistError, Option[Account]]] = {
         Future(PersistResponse.success(Some(generated)))
       }
     }

    val persist = new Service[Future, (Account, TransactionContext), Either[PersistError, Account]] {
       def apply(request: (Account, TransactionContext)): Future[Either[PersistError, Account]] = {
         val (a, t) = request
         Future(PersistResponse.success(
           a.copy(transactions = a.transactions :+ Transaction.fromCtx(transactionId, t))))
       }
     }

    val args = (generatedId, transactionCtx)
    val service = new AddTransactionAccountService(lookup, persist)
    val result = Await.result(service(args), Duration.Inf)
    result.transactions.head should be(transaction)
  }
}
