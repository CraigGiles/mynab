package com.gilesc
package endpoint

import io.finch._
import io.circe.generic.auto._
import io.finch.circe._

import com.gilesc.mynab.transaction.Transaction
import com.gilesc.mynab.category.MajorCategory
import com.gilesc.mynab.category.MinorCategory
import com.gilesc.mynab.category.Category

import com.gilesc.mynab.PresentationData._

// import com.twitter.finagle.stats.Counter
object TransactionEndpoint {
  val path = "transactions"
  // val transactions: Counter = statsReceiver.counter("transactions")

  def getTransactions: Endpoint[Vector[TransactionData]] = get(path) {
    Ok(Vector.empty[TransactionData])
  }

  def postTransaction: Endpoint[TransactionData] = post(path :: jsonBody[TransactionData]) {
    t: TransactionData =>

      import com.gilesc.mynab.transaction._
      import java.time._

    val trans = Transaction(TransactionId(1L),
      LocalDate.parse(t.date),
      Payee(t.payee),
      Category(MajorCategory(t.category.major), MinorCategory(t.category.minor)),
      Memo(t.memo),
      Amount(BigDecimal(t.withdrawal)),
      Amount(BigDecimal(t.deposit)),
      Cleared(false))

    // val trans = Transaction.save(t)
    // transactions.incr()

    val transData = TransactionData(
      trans.id.toString,
      trans.date.toString,
      trans.payee.value,
      CategoryData(trans.category.major.value, trans.category.minor.value),
      trans.memo.value,
      trans.withdrawal.value.toDouble,
      trans.deposit.value.toDouble,
      trans.cleared.value)

    Created(transData)
  }

}
