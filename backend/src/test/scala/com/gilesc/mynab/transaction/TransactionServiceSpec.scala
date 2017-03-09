package com.gilesc.mynab.transaction

import java.time.LocalDate

import com.gilesc.mynab._
import com.gilesc.mynab.logging.PrintlnLoggingService
import com.gilesc.mynab.repository.InMemoryAccountRepository

class TransactionServiceSpec extends TestCase
  with TestCaseHelpers
  with TransactionServiceModule
  with MockTransactionCreation {

  val create: TransactionDetails => CreateResult = TransactionService.create(
    InMemoryAccountRepository.find,
    InMemoryTransactionRepository.save,
    PrintlnLoggingService)

  "Adding a new transaction" should {
    val accountId = 1L
    val date = "2017-02-15"
    val payee = "Uncle Bob"
    val majorCat = "Education"
    val minorCat = "Books"
    val memo = "New programming book from Uncle Bob"
    val deposit = 0.0
    val withdrawal = 39.99
    val cleared = false
    val transactionId = 1L

    val transaction = trans(transactionId, LocalDate.parse(date), payee, majorCat,
      minorCat, memo, withdrawal, deposit, cleared)

    "add the transaction to the accounts transaction list" in {
      val transactionId = 1L
      val details = TransactionDetails(accountId, transactionId, date, payee,
        majorCat, minorCat, memo, deposit, withdrawal, cleared)

      val result = create(details)

      result should be(Success(
        trans(transactionId, LocalDate.parse(date), payee,
          majorCat, minorCat, memo, withdrawal, deposit, cleared)))
    }

    "give you a Left when parsing incorrect formatted date" in {
      val date = "20170215"
      val expected = s"Text '$date' could not be parsed at index 0"
      TransactionService.parseDate(date) should be(Left(expected))

      val emptyDate = "20170215"
      val emptyExpected = s"Text '$emptyDate' could not be parsed at index 0"
      TransactionService.parseDate(date) should be(Left(emptyExpected))
    }

    "convert the input details into a transaction" in {
      val transactionId = 1L
      val details = TransactionDetails(accountId, transactionId, date, payee,
        majorCat, minorCat, memo, deposit, withdrawal, cleared)
      val result = TransactionService.convert(details)
      result should be(Right(transaction))
    }

    "append the transaction to the proper account" in {
      // TODO: prepend the transaction to accounts transaction list
      val accountId = 1L
      val transactionId = 1L
      val details = TransactionDetails(accountId, transactionId, date, payee,
        majorCat, minorCat, memo, deposit, withdrawal, cleared)

      println(details)
    }
  }

}
