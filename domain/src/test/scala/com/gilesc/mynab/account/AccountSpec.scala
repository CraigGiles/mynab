package com.gilesc.mynab
package account

import com.gilesc.mynab.transaction.Transaction

class AccountSpec extends TestCase
  with MockTransactionCreation
  with MockAccountCreation
  with TestCaseHelpers {

  "An account" should "have the ability to prepend transactions" in {
      val payeeInfo = "mynab-test"
      val trans01 = trans(withdrawal = 0.0, deposit = 0.0)
      val trans02 = trans(withdrawal = 0.0, deposit = 1.0)
      val state0 = Vector(trans01)
      val state1 = Vector(trans02, trans01)

      Account.prepend(trans01, Vector.empty[Transaction]) should be(state0)
      Account.prepend(trans02, state0) should be(state1)

      val account = banking("Chase Banking", Vector.empty[Transaction])
      account.transactions should be(Vector.empty[Transaction])
      val newacc = Account.add(trans(payee = payeeInfo, withdrawal = 100.0)).runS(account).value
      newacc.transactions.size should be(1)
      newacc.transactions.head.payee.value should be(payeeInfo)
    }

    it should "have the ability to remove a specific transaction" in {
      val t0 = trans(deposit = 0.0, withdrawal = 0.0)
      val t1 = trans(deposit = 0.0, withdrawal = 1.0)
      val t2 = trans(deposit = 0.0, withdrawal = 2.0)

      val state = Vector(t2, t1, t0)
      val expected = Vector(t2, t0)

      Account.remove(t1, state) should be(expected)
    }

    it should "give me a proper account type object" in {
      AccountType("Banking") should be(Right(Banking))
      AccountType("banking") should be(Right(Banking))
      AccountType("BANKING") should be(Right(Banking))
    }
}
