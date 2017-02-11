package com.gilesc.mynab

import java.time.LocalDate

import com.gilesc.mynab.transaction._
import com.gilesc.mynab.transaction.Transaction._

trait MockTransactionCreation {
  def t(withdrawal: Double, deposit: Double): Transaction = {
    Transaction(LocalDate.now(),
      Payee("Me"),
      Category.apply(MajorCategory("loans"), MinorCategory("student loan")),
      Memo(""),
      Amount(BigDecimal(withdrawal)),
      Amount(BigDecimal(deposit)),
      Cleared(false))
  }
}

class TransactionSpec extends TestCase with MockTransactionCreation {
  "Transactions" should {
    "be summable" in {

      val ts = List(t(0.0,0.0), t(0.0,1.0), t(0.0,2.0), t(0.0,3.0), t(0.0,4.0), t(0.0,5.0))
      val ts2 = List(t(10.0,0.0), t(0.0,1.0), t(0.0,2.0), t(0.0,3.0), t(0.0,4.0), t(0.0,5.0))

      sumTransactions(List.empty[Transaction]) should be(BigDecimal.apply(0.0))
      sumTransactions(ts) should be(BigDecimal(15.0))
      sumTransactions(ts2) should be(BigDecimal(5.0))

   }
  }
}
