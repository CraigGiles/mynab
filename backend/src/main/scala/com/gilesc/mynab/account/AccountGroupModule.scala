package com.gilesc.mynab.account

import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction.Transaction._

trait AccountGroupModule { self: Prepending with Removing =>
  type AccountState = Vector[Account]

  def sumAllAccounts: AccountState => BigDecimal =
    _.foldRight(BigDecimal(0)) { (acc, sum) =>
      sumTransactions(acc.transactions) + sum
  }
}
