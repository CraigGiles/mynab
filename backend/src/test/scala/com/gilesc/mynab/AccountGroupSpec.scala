package com.gilesc.mynab
package account

class AccountGroupSpec extends TestCase with MockTransactionCreation with MockAccountCreation {
  import AccountGroup._

  "Account groups" should {
    "allow the addition of new accounts" in {
      val al = Vector.empty[Account]
      val b = banking("chase", Vector(t(0.0, 1000.0)))
      val l = loan("chase", Vector(t(0.0, 1000.0)))

      // append to an empty account list
      prepend(b, al) should be(Vector(b))

      // ensure its appending not adding to end
      val state0 = prepend(b, al)
      val state1 = prepend(l, state0)

      state0 should be(Vector(b))
      state1 should be(Vector(l, b))
    }

    "allow the removal of new accounts" in {
      val b = banking("chase", Vector(t(0.0, 1000.0)))
      val l = loan("chase", Vector(t(0.0, 1000.0)))
      val bl = Vector(b, l)

      val state0 = remove(b, bl)
      val state1 = remove(l, bl)
      val state2 = remove(b, state1)

      state0 should be(Vector(l))
      state1 should be(Vector(b))
      state2 should be(Vector.empty[Account])
      remove(b, state0) should be(Vector(l))
    }

    "sum the total transaction list from all its accounts" in {
      val b = banking("chase", Vector(t(0.0, 1000.0)))
      val l = loan("nelnet", Vector(t(5000.0, 0.0)))
      val i = banking("chase-savings", Vector(t(0.0, 14000)))
      val al = Vector(b, l)
      val al2 = Vector(b, l, i)

      sumAllAccounts(al) should be(BigDecimal(-4000.0))
      sumAllAccounts(al2) should be(BigDecimal(10000.0))
    }
  }
}
