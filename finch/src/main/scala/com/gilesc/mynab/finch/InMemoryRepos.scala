package com.gilesc.mynab.finch

import com.gilesc.mynab.account._

object InMemoryRepos {
  object GroupsRepo {
    var groups = Vector.empty[AccountGroup]

    def find(id: AccountGroupId): Option[AccountGroup] = groups.find(_.id == id)
  }

  object AccountsRepo {
    var accounts = Vector.empty[Account]

    def save(name: AccountName, t: AccountType): Either[AccountPersistenceError, AccountId] = {
      val id = AccountId(accounts.length.toLong)
      val account = Account.create(id, t, name)
      accounts = accounts :+ account
      Right(id)
    }
  }

}
