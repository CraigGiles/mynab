package com.gilesc
package mynab

import com.gilesc.mynab.account._

class GettingAccountDetailsSpec extends TestCase
  with TestCaseHelpers
  with MockAccountCreation {

  "Finding an account in the system" should "give you None if no account exits" in {
    val id = 1L
    def mockFind(id: AccountId): Option[Account] = None

    val account = AccountService.find(mockFind)(id)
    account should be(None)
  }

  it should "give you an account with all of its transactions" in {
    val id = 1L
    val transaction01 = createTransaction(1L, "East Bay Municipal District", "Housing", "Water", "", 105.26, 0.0)
    val transaction02 = createTransaction(2L, "Credit Karma", "Income", "This Month", "", 0, 3742.56)
    val chasechecking = "Chase Checking"
    val accountType = Banking
    val expected = Account(id, accountType, chasechecking, Vector(transaction02, transaction01))

    def mockFind(id: AccountId): Option[Account] = {
      val chaseChecking = Account(id, accountType, chasechecking, Vector(transaction02, transaction01))
      Option(chaseChecking)
    }

    val account = AccountService.find(mockFind)(id)
    account should be(Some(expected))
  }
}
