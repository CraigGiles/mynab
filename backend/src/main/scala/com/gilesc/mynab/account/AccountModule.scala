package com.gilesc.mynab.account

import cats._
import cats.implicits._
import cats.data.State
import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction.Transaction

trait AccountModule { self: Prepending with Removing =>

  val addTransaction: Transaction => State[Account, Unit] = trans =>
    State[Account, Unit] { acc =>
      (acc.copy(acc.name, prepend(trans, acc.transactions)), ())
  }

  val newAccount: (AccountType, String) => Account = (t, s) =>
    Account(t, s, Vector.empty[Transaction])
}
