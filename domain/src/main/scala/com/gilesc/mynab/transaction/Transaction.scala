package com.gilesc.mynab.transaction

import java.time.{LocalDate => Date}

import com.gilesc.mynab.category._

case class Payee(value: String) extends AnyVal

case class Memo(value: String) extends AnyVal
case class Amount(value: BigDecimal) extends AnyVal
case class Cleared(value: Boolean) extends AnyVal

case class Transaction(date: Date, payee: Payee, category: Category,
  memo: Memo, withdrawal: Amount, deposit: Amount, cleared: Cleared)

object Transaction extends TransactionModule {
  import java.time.LocalDate
  def apply(payee: String,
            majorCategory: String,
            minorCategory: String,
            memo: String,
            withdrawal: Double,
            deposit: Double): Transaction = {

    Transaction(LocalDate.now(),
      Payee(payee),
      Category.apply(MajorCategory(majorCategory), MinorCategory(minorCategory)),
      Memo(memo),
      Amount(BigDecimal(withdrawal)),
      Amount(BigDecimal(deposit)),
      Cleared(false))
  }


  val toggleCleared: (Vector[Transaction], TransactionState) => TransactionState =
    (t, s) => s.flatMap { tr =>
      t.map { ti =>
        if (tr == ti) ti.copy(cleared = Cleared(!tr.cleared.value)) else tr
      }
    }

  val sum: Vector[Transaction] => BigDecimal =
    _.foldRight(BigDecimal(0.0)) { (t, sum) =>
      t.deposit.value - t.withdrawal.value + sum
    }

  val recategorize: (Category, Vector[Transaction]) => Vector[Transaction] = (c, ts) =>
    ts.map(_.copy(category = c))
}

