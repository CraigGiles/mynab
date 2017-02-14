package com.gilesc.mynab
package account

import com.gilesc.mynab.transaction.{Cleared, Transaction}

class AccountSpec extends TestCase with MockTransactionCreation {
  import Account._

  "An account" should {
    "have the ability to prepend transactions" in {
      val state0 = Vector(t(0.0,0.0))
      val state1 = Vector(t(0.0,1.0), t(0.0,0.0))

      prepend(t(0.0,0.0), Vector.empty[Transaction]) should be(state0)
      prepend(t(0.0,1.0), state0) should be(state1)
    }

    "have the ability to remove a specific transaction" in {
      val t1 = t(0.0,1.0)
      val state = Vector(t(0.0,2.0), t1, t(0.0,0.0))
      val expected = Vector(t(0.0,2.0), t(0.0,0.0))

      remove(t1, state) should be(expected)
    }

    "have the ability to mark transactions as 'cleared'" in {
      val t1 = t(0.0,1.0)
      val state = Vector(t(0.0,2.0), t1)
      val expected = Vector(t(0.0,2.0), t1.copy(cleared = Cleared(true)))

      toggleCleared(Vector(t1), Vector(t1)) should be(Vector(t1.copy(cleared = Cleared(true))))
      toggleCleared(Vector(t1), state) should be(expected)
    }
  }
}
