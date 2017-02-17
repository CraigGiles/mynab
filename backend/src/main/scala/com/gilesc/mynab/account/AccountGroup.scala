package com.gilesc.mynab.account

import cats.data.State
import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction._

object AccountGroup extends AccountGroupModule with Prepending with Removing {
  type AccountState = Vector[Account]
  import AccountImplicits._

  val create: (String) => AccountGroup =
    AccountGroup(_, Vector.empty[Account])

  val add: Account => State[AccountGroup, Unit] = a =>
    State[AccountGroup, Unit] { group =>
      (group.copy(accounts = prepend(a, group.accounts)), ())
    }

  val sumAllAccounts: AccountState => BigDecimal =
    _.foldRight(BigDecimal(0)) { (acc, sum) =>
      Transaction.sum(acc.transactions) + sum
    }
}

case class AccountGroup(name: AccountName, accounts: Vector[Account]) {
  def transactions: Vector[Transaction] = accounts.head.transactions
}

