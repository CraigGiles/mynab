package com.gilesc.mynab.transaction

import java.time.LocalDate

import com.gilesc.mynab._

class TransactionServiceSpec extends TestCase
  with TestCaseHelpers
  with MockTransactionCreation {

  def add: TransactionDetails => String = d => "hello"

  "Adding a new transaction" should {
    "add the transaction to the accounts transaction list" in {
//      val date = "2017-02-15"
//      val payee = "Uncle Bob"
//      val majorCat = "Education"
//      val minorCat = "Books"
//      val memo = "New programming book from Uncle Bob"
//      val deposit = 0.0
//      val withdrawl = 39.99
//      val cleared = false
//
//      val details = TransactionDetails(date, payee, majorCat, minorCat, memo, deposit, withdrawl, cleared)
//      val newState = add(details)
//      println(details)
//      println(newState)
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
      val date = "2017-02-15"
      val payee = "Uncle Bob"
      val majorCat = "Education"
      val minorCat = "Books"
      val memo = "New programming book from Uncle Bob"
      val deposit = 0.0
      val withdrawal = 39.99
      val cleared = false

      val details = TransactionDetails(date, payee, majorCat, minorCat, memo, deposit, withdrawal, cleared)
      val transaction = TransactionService.convert(details)
      val expected = trans(LocalDate.parse(date), payee, majorCat, minorCat, memo, withdrawal, deposit, cleared)
      transaction should be(Right(expected))
    }
  }

}
