package com.gilesc.mynab

import com.gilesc.mynab.category._
import com.gilesc.mynab.transaction.Transaction._
import com.gilesc.mynab.transaction._

class TransactionSpec extends TestCase with MockTransactionCreation {
  "Transactions" should {
    "have deposits sum correctly" in {
      val ts = List(t(0.0,0.0), t(0.0,1.0), t(0.0,2.0),
        t(0.0,3.0), t(0.0,4.0), t(0.0,5.0))

      sumTransactions(List.empty[Transaction]) should be(BigDecimal.apply(0.0))
      sumTransactions(ts) should be(BigDecimal(15.0))
    }

    "have withdrawls sum correctly" in {
      val ts = List(t(10.0,0.0), t(2.0, 0.0), t(0.0,2.0),
        t(0.0,3.0), t(0.0,4.0), t(0.0,5.0))

      sumTransactions(ts) should be(BigDecimal(2.0))
    }

    "allow you to change the category" in {
      val ts = List(t(0.0, 1.0))
      val major = MajorCategory("loans")
      val minor = MinorCategory("student")
      val c = Category(major, minor)

      val changed = recategorize(c, ts)
      changed.head.category.major should be(major)
      changed.head.category.minor should be(minor)
    }
  }
}
