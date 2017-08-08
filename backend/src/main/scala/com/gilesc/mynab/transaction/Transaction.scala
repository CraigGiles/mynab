package com.gilesc
package mynab
package transaction

import com.gilesc.mynab.category.Category

import java.time.{LocalDate => Date}

case class TransactionId(value: Long) extends AnyVal
case class Payee(value: String) extends AnyVal

case class Memo(value: String) extends AnyVal
case class Amount(value: BigDecimal) extends AnyVal
case class Cleared(value: Boolean) extends AnyVal

case class Transaction(id: TransactionId, date: Date, payee: Payee,
  category: Category, memo: Memo, withdrawal: Amount, deposit: Amount,
  cleared: Cleared)

object Transaction {
  case class TransactionContext()

  // def save(ctx: TransactionContext): Option[Transaction] = {
  // }
}
