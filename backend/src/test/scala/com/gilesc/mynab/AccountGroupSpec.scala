package com.gilesc.mynab
package account

class AccountGroupSpec extends TestCase with MockTransactionCreation with MockAccountCreation {
  import AccountGroup._

  "Account groups" should {
    "allow the addition of new accounts" in {
      val al = List.empty[Account]
      val b = banking("chase", List(t(0.0, 1000.0)))
      val l = loan("chase", List(t(0.0, 1000.0)))

      // append to an empty account list
      prepend(b, al) should be(List(b))

      // ensure its appending not adding to end
      val state0 = prepend(b, al)
      val state1 = prepend(l, state0)

      state0 should be(List(b))
      state1 should be(List(l, b))
    }
  }
}
