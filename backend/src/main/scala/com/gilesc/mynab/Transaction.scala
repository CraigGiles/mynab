package com.gilesc.mynab

object Transaction {
  import java.time.{LocalDate => Date}

  case class Payee(value: String) extends AnyVal

  case class MajorCategory(value: String) extends AnyVal
  case class MinorCategory(value: String) extends AnyVal
  case class Category(major: MajorCategory, minor: MinorCategory)

  case class Memo(value: String) extends AnyVal
  case class Amount(value: BigDecimal) extends AnyVal {
    def +(other: BigDecimal): Amount = Amount(value + other)
  }
  case class Cleared(value: String) extends AnyVal

  case class Transaction(date: Date, payee: Payee, category: Category,
    memo: Memo, amount: Amount, cleared: Cleared)

  val sumTransactions: List[Transaction] => BigDecimal =
    _.foldRight(BigDecimal(0.0))((t, s) => t.amount.value + s)
}
