package com.gilesc.mynab
package transaction

import java.time.{LocalDate => Date}

import com.gilesc.mynab.category._

trait TransactionModule {
  def sumTransactions: Vector[Transaction] => BigDecimal
  def recategorize: (Category, Vector[Transaction]) => Vector[Transaction]
}

object Transaction extends TransactionModule {
  val sumTransactions: Vector[Transaction] => BigDecimal =
    _.foldRight(BigDecimal(0.0)) { (t, sum) =>
      t.deposit.value - t.withdrawal.value + sum
    }

  val recategorize: (Category, Vector[Transaction]) => Vector[Transaction] = (c, ts) =>
    ts.map(_.copy(category = c))
}

// Transaction Domain Objects
// ------------------------------------------------------------------------
case class Payee(value: String) extends AnyVal

case class Memo(value: String) extends AnyVal
case class Amount(value: BigDecimal) extends AnyVal
case class Cleared(value: Boolean) extends AnyVal

case class Transaction(date: Date, payee: Payee, category: Category,
  memo: Memo, withdrawal: Amount, deposit: Amount, cleared: Cleared)
