package com.gilesc.mynab

class AccountSpec extends TestCase with MockTransactionCreation {
  import AccountDomain._
  import Account._

  "An account" should {
    "have the ability to prepend transactions" in {
      val state0 = List(t(0.0))
      val state1 = List(t(1.0), t(0.0))

      prependTransaction(t(0.0), Nil) should be(state0)
      prependTransaction(t(1.0), state0) should be(state1)
    }
  }
}
