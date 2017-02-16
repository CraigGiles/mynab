package com.gilesc.mynab.account

import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction.{Cleared, Transaction}

trait AccountModule { self: Prepending with Removing =>
  type TransactionState = Vector[Transaction]

  def toggleCleared: (Vector[Transaction], TransactionState) => TransactionState =
    (t, s) => s.flatMap { tr =>
      t.map { ti =>
        if (tr == ti) ti.copy(cleared = Cleared(!tr.cleared.value)) else tr
      }
    }

  val addTransaction: (Transaction, Account) => Account = (t, a) =>
    a.copy(a.name, prepend(t, a.transactions))
}
