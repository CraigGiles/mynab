package com.gilesc.mynab
package transaction

import java.time.{LocalDate => Date}

trait TransactionModule {
  def sumTransactions: List[Transaction] => BigDecimal
  def changeCategory: (Category, List[Transaction]) => List[Transaction]
}

object Transaction {
  val sumTransactions: List[Transaction] => BigDecimal =
    _.foldRight(BigDecimal(0.0)) { (t, sum) =>
      t.deposit.value - t.withdrawal.value + sum
    }

  val changeCategory: (Category, List[Transaction]) => List[Transaction] = (c, ts) =>
    ts.map(_.copy(category = c))
}

// Transaction Domain Objects
// ------------------------------------------------------------------------
case class Payee(value: String) extends AnyVal

case class MajorCategory(value: String) extends AnyVal
case class MinorCategory(value: String) extends AnyVal
case class Category(major: MajorCategory, minor: MinorCategory)

case class Memo(value: String) extends AnyVal
case class Amount(value: BigDecimal) extends AnyVal {
  def +(other: BigDecimal): Amount = Amount(value + other)
}
case class Cleared(value: Boolean) extends AnyVal

case class Transaction(date: Date, payee: Payee, category: Category,
  memo: Memo, withdrawal: Amount, deposit: Amount, cleared: Cleared)
