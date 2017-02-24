package com.gilesc.mynab.transaction

import java.time.LocalDate

import com.gilesc.mynab._
import com.gilesc.mynab.logging.PrintlnLoggingService

class TransactionServiceSpec extends TestCase
  with TestCaseHelpers
  with TransactionServiceModule
  with MockTransactionCreation {

  val create = TransactionService.create(InMemoryTransactionRepository, PrintlnLoggingService)

  "Adding a new transaction" should {
    val date = "2017-02-15"
    val payee = "Uncle Bob"
    val majorCat = "Education"
    val minorCat = "Books"
    val memo = "New programming book from Uncle Bob"
    val deposit = 0.0
    val withdrawal = 39.99
    val cleared = false

    "add the transaction to the accounts transaction list" in {
      val details = TransactionDetails(date, payee, majorCat, minorCat, memo, deposit, withdrawal, cleared)
      val expected = trans(LocalDate.parse(date), payee, majorCat, minorCat, memo, withdrawal, deposit, cleared)

      val result = create(details)

      result should be(Success(expected))
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
      val details = TransactionDetails(date, payee, majorCat, minorCat, memo, deposit, withdrawal, cleared)
      val transaction = TransactionService.convert(details)
      val expected = trans(LocalDate.parse(date), payee, majorCat, minorCat, memo, withdrawal, deposit, cleared)
      transaction should be(Right(expected))
    }
  }

}
