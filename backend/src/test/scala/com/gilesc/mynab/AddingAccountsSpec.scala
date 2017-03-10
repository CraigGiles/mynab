package com.gilesc
package mynab

import cats.implicits._
import com.gilesc.mynab.account._
import org.scalatest._

class AddingAccountsSpec extends FlatSpec with Matchers
  with MockAccountCreation
  with TestCaseHelpers
  with AccountGroupService {

  val createGroup: AccountName => Either[String, AccountGroup] =
    AccountGroupService.create(InMemoryAccountGroups.save)

  val createAccount: AccountContext => Either[AccountPersistenceError, AccountGroup] =
    AccountService.create(InMemoryAccounts.save, InMemoryAccountGroups.find)

  "Creating an account group by name" should "return the account group" in {
    val budgetaccounts = "Budget Accounts"
    val nonbudgetaccounts = "Non Budget Accounts"

    createGroup(budgetaccounts) should be(Right(AccountGroup(1L, budgetaccounts, Vector.empty[Account])))
    createGroup(nonbudgetaccounts) should be(Right(AccountGroup(2L, nonbudgetaccounts, Vector.empty[Account])))
  }

  "Creating a new account" should "add the account to a valid account group" in {
    val budgetaccounts = "Budget Accounts"
    val groupId = (createGroup(budgetaccounts) map(_.id)).toOption.get

    val name = "Chase Checking"
    val groupname = "Budget Accounts"
    val accType = Banking
    val accountId = 1L
    val ctx = AccountContext(groupId, name, accType)
    val group = createAccount(ctx)
    val expectedAccounts = Vector(Account.create(accountId, accType, name))

    group should be(Right(AccountGroup(groupId, groupname, expectedAccounts)))
  }

  it should "give the proper error if no account group can be found" in {
    val groupId = -1L

    val name = "Chase Checking"
    val accType = Banking
    val ctx = AccountContext(groupId, name, accType)
    val group = createAccount(ctx)

    group should be(Left(InvalidAccountGroupId(groupId)))
  }
}
