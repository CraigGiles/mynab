package com.gilesc.mynab.account

import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction.Transaction

trait AccountModule { self: Prepending with Removing =>

  val addTransaction: (Transaction, Account) => Account = (t, a) =>
    a.copy(a.name, prepend(t, a.transactions))
}
