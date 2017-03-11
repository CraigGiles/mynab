package com.gilesc.mynab

import java.time.LocalDate

import com.gilesc.mynab.account._
import com.gilesc.mynab.category._
import com.gilesc.mynab.transaction._

class AddingTransactionsSpec extends TestCase
  with TestCaseHelpers
  with MockTransactionCreation
  with MockAccountCreation {

  "Creating a new transaction" should "add the transaction to the valid account" in {
    def mockSave(ctx: TransactionContext) = Right(TransactionId(1L))
    def mockFind(id: AccountId): Option[Account] =
      Option(bankingWithId(id.value, "Mocked Banking Account", Vector.empty[Transaction]))

    val accountId = 1L
    val date = LocalDate.now()
    val payee = Payee("Mock Payee")
    val category = Category(MajorCategory("Mock"), MinorCategory("Category"))
    val memo = Memo("Mock Memo")
    val deposit = Amount(BigDecimal(0.0))
    val withdrawal = Amount(BigDecimal(0.0))
    val cleared = Cleared(false)
    val ctx = TransactionContext(accountId, date, payee, category, memo, deposit, withdrawal, cleared)

    val transaction = Transaction.create(1L, ctx)
    val account = TransactionService.create(mockSave, mockFind)(ctx)

    account should be(
      Right(BankingAccount(1L, "Mocked Banking Account", Vector(transaction)))
    )
  }
}
