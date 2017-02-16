package com.gilesc.mynab
package account

import cats.data.State
import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction.Transaction._

trait AccountGroupModule { self: Prepending with Removing =>
  type AccountState = Vector[Account]
  import AccountImplicits._

  val newAccountGroup: (String) => AccountGroup =
    AccountGroup(_, Vector.empty[Account])

  val addAccount: Account => State[AccountGroup, Unit] = a =>
    State[AccountGroup, Unit] { group =>
      (group.copy(accounts = prepend(a, group.accounts)), ())
  }

  val sumAllAccounts: AccountState => BigDecimal =
    _.foldRight(BigDecimal(0)) { (acc, sum) =>
      sumTransactions(acc.transactions) + sum
  }
}
