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
  def apply(date: LocalDate,
            payee: String,
            majorCategory: String,
            minorCategory: String,
            memo: String,
            withdrawal: Double,
            deposit: Double,
            cleared: Boolean): Transaction = {

    Transaction(date,
      Payee(payee),
      Category.apply(MajorCategory(majorCategory), MinorCategory(minorCategory)),
      Memo(memo),
      Amount(BigDecimal(withdrawal)),
      Amount(BigDecimal(deposit)),
      Cleared(cleared))
  }
}

