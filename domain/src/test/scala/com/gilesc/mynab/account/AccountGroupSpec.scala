package com.gilesc.mynab.account

import com.gilesc.mynab.transaction.Transaction
import com.gilesc.mynab.{MockAccountCreation, MockTransactionCreation, TestCase}

class AccountGroupSpec extends TestCase with MockTransactionCreation with MockAccountCreation {
  import AccountGroup._
  implicit def str2AccountName(str: String): AccountName = AccountName(str)

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

      sum(al) should be(BigDecimal(-4000.0))
      sum(al2) should be(BigDecimal(10000.0))
    }

    "should allow me to create accounts and transactions cleanly" in {
      val checkingTransactions = for {
        _ <- Account.add(Transaction("East Bay Municipal District", "Housing", "Water", "", 105.26, 0.0))
        _ <- Account.add(Transaction("Credit Karma", "Income", "This Month", "", 0, 3742.56))
      } yield ()
      val chaseChecking = checkingTransactions.runS(Account.create(Banking, "Chase Checking")).value

      val visaTransactions = for {
        _ <- Account.add(Transaction("Frontpoint Security", "Housing", "Security", "", 45.00, 0.0))
        _ <- Account.add(Transaction("Netflix", "Lifestyle", "Movies", "", 9.99, 0.0))
        _ <- Account.add(Transaction("Comcast", "Lifestyle", "Internet", "", 65.00, 0.0))
        _ <- Account.add(Transaction("T-Mobile", "Lifestyle", "Cell Phone", "", 111.12, 0.0))
      } yield ()
      val chaseVisaAmazon = visaTransactions.runS(Account.create(Banking, "Chase Amazon CC")).value

      val accounts = for {
        _ <- AccountGroup.add(chaseChecking)
        _ <- AccountGroup.add(chaseVisaAmazon)
      } yield ()
      val group = accounts.runS(AccountGroup.create("Budget Accounts")).value

      AccountGroup.sum(group.accounts) should be(3406.19)

    }
  }
}
