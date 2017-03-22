package com.gilesc.mynab.transaction

import java.time.{LocalDate => Date}

import com.gilesc.mynab.account.AccountId
import com.gilesc.mynab.category._

case class TransactionId(value: Long) extends AnyVal
case class Payee(value: String) extends AnyVal

case class Memo(value: String) extends AnyVal
case class Amount(value: BigDecimal) extends AnyVal
case class Cleared(value: Boolean) extends AnyVal

case class Transaction(id: TransactionId, date: Date, payee: Payee,
  category: Category, memo: Memo, withdrawal: Amount, deposit: Amount,
  cleared: Cleared)

case class TransactionContext(
  accountId: AccountId,
  date: Date, payee: Payee, category: Category, memo: Memo,
  deposit: Amount, withdrawal: Amount, cleared: Cleared)

object Transaction extends TransactionModule {

  def create(transactionId: TransactionId, ctx: TransactionContext) = {
    Transaction(transactionId, ctx.date, ctx.payee, ctx.category, ctx.memo,
      ctx.withdrawal, ctx.deposit, ctx.cleared)
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

