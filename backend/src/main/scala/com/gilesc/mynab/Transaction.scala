package com.gilesc.mynab
package transaction

import java.time.{LocalDate => Date}

import com.gilesc.mynab.category._

trait TransactionModule {
  def sumTransactions: List[Transaction] => BigDecimal
  def recategorize: (Category, List[Transaction]) => List[Transaction]
}

object Transaction extends TransactionModule {
  val sumTransactions: List[Transaction] => BigDecimal =
    _.foldRight(BigDecimal(0.0)) { (t, sum) =>
      t.deposit.value - t.withdrawal.value + sum
    }

  val recategorize: (Category, List[Transaction]) => List[Transaction] = (c, ts) =>
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
