package com.gilesc.mynab.transaction

import com.gilesc.mynab.category.Category

trait TransactionModule {
  type TransactionState = Vector[Transaction]

  def toggleCleared: (Vector[Transaction], TransactionState) => TransactionState =
    (t, s) => s.flatMap { tr =>
      t.map { ti =>
        if (tr == ti) ti.copy(cleared = Cleared(!tr.cleared.value)) else tr
      }
    }

  val sumTransactions: Vector[Transaction] => BigDecimal =
    _.foldRight(BigDecimal(0.0)) { (t, sum) =>
      t.deposit.value - t.withdrawal.value + sum
    }

  val recategorize: (Category, Vector[Transaction]) => Vector[Transaction] = (c, ts) =>
    ts.map(_.copy(category = c))
}
