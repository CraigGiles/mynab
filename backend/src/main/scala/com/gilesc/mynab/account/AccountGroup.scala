package com.gilesc.mynab.account

import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction._

object AccountGroup extends AccountGroupModule with Prepending with Removing {
}

case class AccountGroup(name: AccountName, accounts: Vector[Account]) {
  def transactions: Vector[Transaction] = accounts.head.transactions
}

