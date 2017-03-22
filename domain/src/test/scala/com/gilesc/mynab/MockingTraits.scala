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

  implicit def str2Payee(value: String): Payee = Payee(value)
  implicit def str2Memo(value: String): Memo = Memo(value)
  implicit def str2MajorCategory(value: String): MajorCategory = MajorCategory(value)
  implicit def str2MinorCategory(value: String): MinorCategory = MinorCategory(value)
  implicit def double2Amount(value: Double): Amount = Amount(BigDecimal(value))

  def createTransaction(id: Long,
    payee: String,
    majorCategory: String,
    minorCategory: String,
    memo: String,
    withdrawal: Double,
    deposit: Double,
    localDate: LocalDate = LocalDate.now()): Transaction = {

    Transaction(
      TransactionId(id),
      localDate,
      Payee(payee),
      Category.apply(MajorCategory(majorCategory), MinorCategory(minorCategory)),
      Memo(memo),
      Amount(BigDecimal(withdrawal)),
      Amount(BigDecimal(deposit)),
      Cleared(false))
  }

}

trait MockAccountCreation { self: TestCaseHelpers =>
  def banking(name: String, transactions: Vector[Transaction]): BankingAccount =
    BankingAccount(Random.nextLong(), name, transactions)
  def loan(name: String, transactions: Vector[Transaction]): LoanAccount =
    LoanAccount(Random.nextLong(), name, transactions)
  def bankingWithId(id: Long, name: String, transactions: Vector[Transaction]): BankingAccount = {
    BankingAccount(id, name, transactions)
  }
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

