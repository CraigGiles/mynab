package com.gilesc.mynab
package account

import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction.Transaction._
import com.gilesc.mynab.transaction._

trait AccountGroupModule { self: Prepending with Removing =>
  type AccountState = List[Account]

  def sumAllAccounts: AccountState => BigDecimal
}

object AccountGroup extends AccountGroupModule with Prepending with Removing {
  def sumAllAccounts: AccountState => BigDecimal = _.foldRight(BigDecimal(0)) { (acc, sum) =>
    sumTransactions(acc.transactions) + sum
  }
}

// Account List Domain Objects
// ------------------------------------------------------------------------
case class AccountGroup(name: AccountName, accounts: List[Account]) {
  def transactions: List[Transaction] = accounts.head.transactions
}

