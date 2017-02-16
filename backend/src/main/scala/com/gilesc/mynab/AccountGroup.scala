package com.gilesc.mynab
package account

import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction.Transaction._
import com.gilesc.mynab.transaction._

trait AccountGroupModule { self: Prepending with Removing =>
  type AccountState = Vector[Account]

  def sumAllAccounts: AccountState => BigDecimal = _.foldRight(BigDecimal(0)) { (acc, sum) =>
    sumTransactions(acc.transactions) + sum
  }
}

object AccountGroup extends AccountGroupModule with Prepending with Removing {
}

// Account Group Domain Objects
// ------------------------------------------------------------------------
case class AccountGroup(name: AccountName, accounts: Vector[Account]) {
  def transactions: Vector[Transaction] = accounts.head.transactions
}

