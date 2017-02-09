package com.gilesc.mynab

import java.time.LocalDate

import org.scalatest.{Matchers, WordSpecLike}

abstract class TestCase extends WordSpecLike with Matchers

class TransactionSpec extends TestCase {
  "Transactions" should {
    "be summable" in {
      import com.gilesc.mynab.Account._
      import com.gilesc.mynab.Transaction._

      val ts = List(
        Transaction(LocalDate.now(), Payee("Me"), Category.apply(MajorCategory("loans"), MinorCategory("student loan")), Memo(""), Amount(BigDecimal(0.0)), Cleared.apply("false")),
        Transaction(LocalDate.now(), Payee("Me"), Category.apply(MajorCategory("loans"), MinorCategory("student loan")), Memo(""), Amount(BigDecimal(1.0)), Cleared.apply("false")),
        Transaction(LocalDate.now(), Payee("Me"), Category.apply(MajorCategory("loans"), MinorCategory("student loan")), Memo(""), Amount(BigDecimal(2.0)), Cleared.apply("false")),
        Transaction(LocalDate.now(), Payee("Me"), Category.apply(MajorCategory("loans"), MinorCategory("student loan")), Memo(""), Amount(BigDecimal(3.0)), Cleared.apply("false")),
        Transaction(LocalDate.now(), Payee("Me"), Category.apply(MajorCategory("loans"), MinorCategory("student loan")), Memo(""), Amount(BigDecimal(4.0)), Cleared.apply("false")),
        Transaction(LocalDate.now(), Payee("Me"), Category.apply(MajorCategory("loans"), MinorCategory("student loan")), Memo(""), Amount(BigDecimal(5.0)), Cleared.apply("false"))
      )

      sumTransactions(List.empty[Transaction]) should be(BigDecimal.apply(0.0))
      sumTransactions(ts) should be(BigDecimal(15.0))
   }
  }
}
