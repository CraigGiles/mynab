package com.gilesc
package mynab

import cats.implicits._
import com.gilesc.mynab.account.{PersistenceFailure, _}
import com.gilesc.mynab.transaction._
import org.scalatest._

class AddingAccountsSpec extends FlatSpec with Matchers
  with MockAccountCreation
  with TestCaseHelpers
  with NewAccountGroupService {

  object GroupRepo {
    import com.gilesc.mynab.account.PersistenceFailure
    private var groups = Vector.empty[AccountGroup]

    def save(name: AccountName): Either[PersistenceFailure, AccountGroupId] = {
      val id = AccountGroupId(groups.size + 1L)
      val group = AccountGroup.create(id, name)

      groups = groups :+ group

      Right(id)
    }

    def find(id: AccountGroupId): Option[AccountGroup] = groups.find(_.id == id)
  }

  object AccRepo {
    import com.gilesc.mynab.account.PersistenceFailure
    private var accounts = Vector.empty[Account]

    def save(ctx: AccountContext): Either[PersistenceFailure, AccountId] = {
      val id = AccountId(accounts.size + 1L)
      val account = Account.create(id, ctx.accType, ctx.name)

      accounts = accounts :+ account

      Right(id)
    }
  }

  val createGroup: AccountName => Either[String, AccountGroup] =
    NewAccountGroupService.create(GroupRepo.save)

  val createAccount: AccountContext => Either[PersistenceFailure, AccountGroup] =
    NewAccountService.create(AccRepo.save, GroupRepo.find)

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
}
