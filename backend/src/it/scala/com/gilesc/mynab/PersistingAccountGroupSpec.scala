package com.gilesc
package mynab

import java.time.LocalDate

import com.gilesc.mynab.account._
import com.gilesc.mynab.category._
import com.gilesc.mynab.transaction._

class PersistingAccountGroupSpec extends TestCase
  with TestCaseHelpers
  with MockTransactionCreation
  with MockAccountCreation {

  "Creating a new account group" should "persist the group in a database" in {
    val id = 1L
    val name = "newaccountgroup"
    val expected = AccountGroup.create(id, name)

    AccountGroupService.createWithPersistence(name) should be(Right(expected))
  }
}
