package com.gilesc.mynab.account

import cats.data.State
import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction.Transaction

trait AccountModule { self: Prepending with Removing =>
  def addTransaction: Transaction => State[Account, Unit]
  def newAccount: (AccountType, String) => Account
}
