package com.gilesc.mynab

import java.time.LocalDate

import com.gilesc.mynab.account._
import com.gilesc.mynab.category._
import com.gilesc.mynab.transaction._

import scala.util.Random
import cats.implicits._

trait TestCaseHelpers {
  implicit def str2accountName(value: String): AccountName = {
    AccountName(value).getOrElse(AccountName("Random Account").toOption.get)
  }

  implicit def long2AccountId(value: Long): AccountId = AccountId(value)
  implicit def long2AccountGroupId(value: Long): AccountGroupId = AccountGroupId(value)
  implicit def long2TransactionId(value: Long): TransactionId = TransactionId(value)
}

trait MockAccountCreation { self: TestCaseHelpers =>
  def banking(name: String, transactions: Vector[Transaction]): BankingAccount =
    BankingAccount(Random.nextLong(), name, transactions)
  def loan(name: String, transactions: Vector[Transaction]): LoanAccount =
    LoanAccount(Random.nextLong(), name, transactions)
}

trait MockTransactionCreation { self: TestCaseHelpers =>
  def trans(
    id: Long = Random.nextLong(),
    date: LocalDate = LocalDate.now(),
    payee: String = "Me",
    majorCategory: String = "loans",
    minorCategory: String = "studen",
    memo: String = "loans",
    withdrawal: Double = 0.0,
    deposit: Double = 0.0,
    cleared: Boolean = false): Transaction = {

    Transaction(
      TransactionId(id),
      date,
      Payee(payee),
      Category.apply(MajorCategory(majorCategory), MinorCategory(minorCategory)),
      Memo(memo),
      Amount(BigDecimal(withdrawal)),
      Amount(BigDecimal(deposit)),
      Cleared(cleared))
  }
}

