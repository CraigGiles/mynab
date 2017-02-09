package com.gilesc.mynab

import java.time.LocalDate
import com.gilesc.mynab.Account._
import com.gilesc.mynab.Transaction._

trait MockTransactionCreation {
  import com.gilesc.mynab.Account._
  import com.gilesc.mynab.Transaction._

  def t(amnt: Double): Transaction = {
    Transaction(LocalDate.now(),
      Payee("Me"),
      Category.apply(MajorCategory("loans"), MinorCategory("student loan")),
      Memo(""),
      Amount(BigDecimal(amnt)),
      Cleared.apply("false"))
  }
}

class TransactionSpec extends TestCase with MockTransactionCreation {
  "Transactions" should {
    "be summable" in {

      val ts = List(t(0.0), t(1.0), t(2.0), t(3.0), t(4.0), t(5.0))

      sumTransactions(List.empty[Transaction]) should be(BigDecimal.apply(0.0))
      sumTransactions(ts) should be(BigDecimal(15.0))
   }
  }
}
