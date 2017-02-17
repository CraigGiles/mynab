package com.gilesc.mynab.transaction

import com.gilesc.mynab.category._
import com.gilesc.mynab.{MockTransactionCreation, TestCase}

class TransactionSpec extends TestCase with MockTransactionCreation {
  "Transactions" should {
    "have deposits sum correctly" in {
      val ts = Vector(t(0.0,0.0), t(0.0,1.0), t(0.0,2.0),
        t(0.0,3.0), t(0.0,4.0), t(0.0,5.0))

      Transaction.sum(Vector.empty[Transaction]) should be(BigDecimal.apply(0.0))
      Transaction.sum(ts) should be(BigDecimal(15.0))
    }

    "have withdrawls sum correctly" in {
      val ts = Vector(t(10.0,0.0), t(2.0, 0.0), t(0.0,2.0),
        t(0.0,3.0), t(0.0,4.0), t(0.0,5.0))

      Transaction.sum(ts) should be(BigDecimal(2.0))
    }

    "allow you to change the category" in {
      val ts = Vector(t(0.0, 1.0))
      val major = MajorCategory("loans")
      val minor = MinorCategory("student")
      val c = Category(major, minor)

      val changed = Transaction.recategorize(c, ts)
      changed.head.category.major should be(major)
      changed.head.category.minor should be(minor)
    }

    "have the ability to mark transactions as 'cleared'" in {
      val t1 = t(0.0,1.0)
      val state = Vector(t(0.0,2.0), t1)
      val expected = Vector(t(0.0,2.0), t1.copy(cleared = Cleared(true)))

      Transaction.toggleCleared(Vector(t1), Vector(t1)) should be(Vector(t1.copy(cleared = Cleared(true))))
      Transaction.toggleCleared(Vector(t1), state) should be(expected)
    }
  }
}
