package com.gilesc.mynab
package account

import com.gilesc.mynab.transaction._

trait MockAccountCreation {
  def banking(name: String, transactions: List[Transaction]): BankingAccount =
    BankingAccount(AccountName(name), transactions)
  def loan(name: String, transactions: List[Transaction]): LoanAccount =
    LoanAccount(AccountName(name), transactions)
}

class AccountGroupSpec extends TestCase with MockTransactionCreation with MockAccountCreation {
  import AccountGroup._

  "Account groups" should {
    "allow the addition of new accounts" in {
      val al = List.empty[Account]
      val b = banking("chase", List(t(0.0, 1000.0)))
      val l = loan("chase", List(t(0.0, 1000.0)))

      // append to an empty account list
      appendAccount(b, al) should be(List(b))

      // ensure its appending not adding to end
      val state0 = appendAccount(b, al)
      val state1 = appendAccount(l, state0)

      state0 should be(List(b))
      state1 should be(List(l, b))
    }
  }
}
