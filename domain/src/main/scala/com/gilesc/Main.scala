package com.gilesc

import com.gilesc.mynab.account._
import com.gilesc.mynab.transaction._

import cats._
import cats.implicits._

object Main extends App {
  implicit def str2accountName(value: String): AccountName = {
    AccountName(value).toOption.getOrElse(AccountName("Random Account").toOption.get)
  }

  implicit def long2AccountId(value: Long): AccountId = AccountId(value)
  implicit def long2TransactionId(value: Long): TransactionId = TransactionId(value)

  def prettyPrint(group: AccountGroup) = {

    println()
    println("----------------------------- ")
    println(s" ${group.name.value}  -  ${AccountGroup.sum(group.accounts)}")
    println("----------------------------- ")
    group.accounts.foreach { account =>
      println(s" > ${account.name.value}  - $$${Transaction.sum(account.transactions)}")
    }
    println("----------------------------- ")
    println()

  }

  val checkingTransactions = for {
    a <- Account.add(Transaction(1L, "East Bay Municipal District", "Housing", "Water", "", 105.26, 0.0))
    b <- Account.add(Transaction(2L, "Credit Karma", "Income", "This Month", "", 0, 3742.56))
  } yield ()
  val chaseChecking = checkingTransactions.runS(Account.create(1L, Banking, "Chase Checking")).value

  val visaTransactions = for {
    _ <- Account.add(Transaction(3L, "Frontpoint Security", "Housing", "Security", "", 45.00, 0.0))
    _ <- Account.add(Transaction(4L, "Netflix", "Lifestyle", "Movies", "", 9.99, 0.0))
    _ <- Account.add(Transaction(5L, "Comcast", "Lifestyle", "Internet", "", 65.00, 0.0))
    _ <- Account.add(Transaction(6L, "T-Mobile", "Lifestyle", "Cell Phone", "", 111.12, 0.0))
  } yield ()
  val chaseVisaAmazon = visaTransactions.runS(Account.create(2L, Banking, "Chase Amazon CC")).value

  val accounts = for {
    _ <- AccountGroup.add(chaseChecking)
    _ <- AccountGroup.add(chaseVisaAmazon)
  } yield ()
  val group = accounts.runS(AccountGroup.create(AccountGroupId(1L), "Budget Accounts")).value

  prettyPrint(group)

}
