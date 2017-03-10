package com.gilesc
package mynab
package account

object InMemoryAccountGroups {
  private var groups = Vector.empty[AccountGroup]

  def save(name: AccountName): Either[AccountGroupPersistenceError, AccountGroupId] = {
    val id = AccountGroupId(groups.size + 1L)
    val group = AccountGroup.create(id, name)

    groups = groups :+ group

    Right(id)
  }

  def find(id: AccountGroupId): Option[AccountGroup] = groups.find(_.id == id)
}

object InMemoryAccounts {
  private var accounts = Vector.empty[Account]

  def save(ctx: AccountContext): Either[AccountPersistenceError, AccountId] = {
    val id = AccountId(accounts.size + 1L)
    val account = Account.create(id, ctx.accType, ctx.name)

    accounts = accounts :+ account

    Right(id)
  }
}


