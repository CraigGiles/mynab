package com.gilesc.mynab
package account

import com.gilesc.mynab.transaction.Cleared

class AccountSpec extends TestCase with MockTransactionCreation {
  import Account._

  "An account" should {
    "have the ability to prepend transactions" in {
      val state0 = List(t(0.0))
      val state1 = List(t(1.0), t(0.0))

      prependTransaction(t(0.0), Nil) should be(state0)
      prependTransaction(t(1.0), state0) should be(state1)
    }

    "have the ability to remove a specific transaction" in {
      val t1 = t(1.0)
      val state = List(t(2.0), t1, t(0.0))
      val expected = List(t(2.0), t(0.0))

      removeTransaction(t1, state) should be(expected)
    }

    "have the ability to mark transactions as 'cleared'" in {
      val t1 = t(1.0)
      val state = List(t(2.0), t1)
      val expected = List(t(2.0), t1.copy(cleared = Cleared(true)))

      toggleCleared(List(t1), List(t1)) should be(List(t1.copy(cleared = Cleared(true))))
      toggleCleared(List(t1), state) should be(expected)
    }
  }
}
