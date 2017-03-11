package com.gilesc.mynab
package account

import com.gilesc.mynab.transaction.Transaction

class AccountGroupSpec extends TestCase
  with MockTransactionCreation
  with MockAccountCreation
  with TestCaseHelpers {

  import AccountGroup._

  "Account groups" should "allow the addition of new accounts" in {
      val al = Vector.empty[Account]
      val b = banking("chase", Vector(trans(withdrawal = 0.0, deposit = 1000.0)))
      val l = loan("chase", Vector(trans(withdrawal = 0.0, deposit = 1000.0)))

      // append to an empty account list
      prepend(b, al) should be(Vector(b))

      // ensure its appending not adding to end
      val state0 = prepend(b, al)
      val state1 = prepend(l, state0)

      state0 should be(Vector(b))
      state1 should be(Vector(l, b))
    }

    it should "allow the removal of new accounts" in {
      val b = banking("chase", Vector(trans(withdrawal = 0.0, deposit = 1000.0)))
      val l = loan("chase", Vector(trans(withdrawal = 0.0, deposit = 1000.0)))
      val bl = Vector(b, l)

      val state0 = remove(b, bl)
      val state1 = remove(l, bl)
      val state2 = remove(b, state1)

      state0 should be(Vector(l))
      state1 should be(Vector(b))
      state2 should be(Vector.empty[Account])
      remove(b, state0) should be(Vector(l))
    }

    it should "sum the total transaction list from all its accounts" in {
      val b = banking("chase", Vector(trans(withdrawal = 0.0, deposit = 1000.0)))
      val l = loan("nelnet", Vector(trans(withdrawal = 5000.0, deposit = 0.0)))
      val i = banking("chase-savings", Vector(trans(withdrawal = 0.0, deposit = 14000)))
      val al = Vector(b, l)
      val al2 = Vector(b, l, i)

      sum(al) should be(BigDecimal(-4000.0))
      sum(al2) should be(BigDecimal(10000.0))
    }

    it should "should allow me to create accounts and transactions cleanly" in {
      val checkingTransactions = for {
        _ <- Account.add(Transaction(1L, "East Bay Municipal District", "Housing", "Water", "", 105.26, 0.0))
        _ <- Account.add(Transaction(2L, "Credit Karma", "Income", "This Month", "", 0, 3742.56))
      } yield ()
      val chaseChecking = checkingTransactions.runS(Account.create(1L, Banking, "Chase Checking")).value

      val visaTransactions = for {
        _ <- Account.add(Transaction(3L, "Frontpoint Security", "Housing", "Security", "", 45.00, 0.0))
        _ <- Account.add(Transaction(4L, "Netflix", "Lifestyle", "Movies", "", 9.99, 0.0))
        _ <- Account.add(Transaction(5L, "Comcast", "Lifestyle", "Internet", "", 65.00, 0.0))
        _ <- Account.add(Transaction(6L, "T-Mobile", "Lifestyle", "Cell Phone", "", 111.12, 0.0))
      } yield ()
      val chaseVisaAmazon = visaTransactions.runS(Account.create(2L, Banking, "Chase Amazon CC")).value

      val accounts = for {
        _ <- AccountGroup.add(chaseChecking)
        _ <- AccountGroup.add(chaseVisaAmazon)
      } yield ()
      val group = accounts.runS(AccountGroup.create(1L, "Budget Accounts")).value

      AccountGroup.sum(group.accounts) should be(3406.19)
    }
}
