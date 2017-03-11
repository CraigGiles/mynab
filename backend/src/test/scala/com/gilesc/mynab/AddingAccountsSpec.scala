package com.gilesc
package mynab

import com.gilesc.mynab.account._

class AddingAccountsSpec extends TestCase
  with MockAccountCreation
  with TestCaseHelpers {

  "Creating an account group by name" should "return the account group" in {
    val groupId = AccountGroupId(1L)
    def mockSave(name: AccountName) = Right(groupId)
    val createGroup = AccountGroupService.create(mockSave) _

    val budgetaccounts = "Budget Accounts"
    val budgetgroup = AccountGroup(groupId, budgetaccounts, Vector.empty[Account])

    val nonbudgetaccounts = "Non Budget Accounts"
    val nonbudgetgroup = AccountGroup(groupId, nonbudgetaccounts, Vector.empty[Account])

    createGroup(budgetaccounts) should be(Right(budgetgroup))
    createGroup(nonbudgetaccounts) should be(Right(nonbudgetgroup))
  }

  "Creating a new account" should "add the account to a valid account group" in {
    val groupId = AccountGroupId(1L)
    val groupname = "Budget Accounts"
    val accountId = 1L
    val name = "Chase Checking"
    val accType = Banking
    val ctx = AccountContext(groupId, name, accType)
    val expectedAccounts = Vector(Account.create(accountId, accType, name))

    def mockSave(ctx: AccountContext) = Right(AccountId(accountId))
    def mockFind(id: AccountGroupId): Option[AccountGroup] =
      Option(AccountGroup(groupId, groupname, Vector.empty[Account]))

    val group = AccountService.create(mockSave, mockFind)(ctx)
    group should be(Right(AccountGroup(groupId, groupname, expectedAccounts)))
  }

  it should "give the proper error if no account group can be found" in {
    val groupId = -1L
    val accountId = 1L
    val name = "Chase Checking"
    val accType = Banking
    val ctx = AccountContext(groupId, name, accType)

    def mockSave(ctx: AccountContext) = Right(AccountId(accountId))
    def mockFind(id: AccountGroupId): Option[AccountGroup] = None

    val group = AccountService.create(mockSave, mockFind)(ctx)

    group should be(Left(InvalidAccountGroupId(groupId)))
  }
}
