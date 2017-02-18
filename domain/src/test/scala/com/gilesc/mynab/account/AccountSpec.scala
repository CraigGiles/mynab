package com.gilesc.mynab
package account

import com.gilesc.mynab.transaction.Transaction
import com.gilesc.mynab._

class AccountSpec extends TestCase
  with MockTransactionCreation
  with MockAccountCreation
  with TestCaseHelpers {

  "An account" should {
    "have the ability to prepend transactions" in {
      val payeeInfo = "mynab-test"
      val state0 = Vector(t(0.0,0.0))
      val state1 = Vector(t(0.0,1.0), t(0.0,0.0))

      Account.prepend(t(0.0,0.0), Vector.empty[Transaction]) should be(state0)
      Account.prepend(t(0.0,1.0), state0) should be(state1)

      val account = banking("Chase Banking", Vector.empty[Transaction])
      account.transactions should be(Vector.empty[Transaction])
      val newacc = Account.add(trans(payee = payeeInfo, withdrawal = 100.0)).runS(account).value
      newacc.transactions.size should be(1)
      newacc.transactions.head.payee.value should be(payeeInfo)
    }

    "have the ability to remove a specific transaction" in {
      val t1 = t(0.0,1.0)
      val state = Vector(t(0.0,2.0), t1, t(0.0,0.0))
      val expected = Vector(t(0.0,2.0), t(0.0,0.0))

      Account.remove(t1, state) should be(expected)
    }

    "give me a proper account type object" in {
      AccountType("Banking") should be(Right(Banking))
      AccountType("banking") should be(Right(Banking))
      AccountType("BANKING") should be(Right(Banking))
    }
  }
}
