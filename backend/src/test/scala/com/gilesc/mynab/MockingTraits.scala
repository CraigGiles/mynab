package com.gilesc.mynab

import java.time.LocalDate

import com.gilesc.mynab.account._
import com.gilesc.mynab.transaction._

trait MockAccountCreation {
  def banking(name: String, transactions: List[Transaction]): BankingAccount =
    BankingAccount(AccountName(name), transactions)
  def loan(name: String, transactions: List[Transaction]): LoanAccount =
    LoanAccount(AccountName(name), transactions)
}

trait MockTransactionCreation {
  def t(withdrawal: Double, deposit: Double): Transaction = {
    Transaction(LocalDate.now(),
      Payee("Me"),
      Category.apply(MajorCategory("loans"), MinorCategory("student loan")),
      Memo(""),
      Amount(BigDecimal(withdrawal)),
      Amount(BigDecimal(deposit)),
      Cleared(false))
  }
}

