package com.gilesc.mynab
package transaction

import com.gilesc.mynab.category._

class TransactionSpec extends TestCase
  with MockTransactionCreation
  with TestCaseHelpers {

  "Transactions" should {
    "have deposits sum correctly" in {
      val ts = Vector(trans(withdrawal = 0.0, deposit = 0.0),
        trans(withdrawal = 0.0, deposit = 1.0),
        trans(withdrawal = 0.0, deposit = 2.0),
        trans(withdrawal = 0.0, deposit = 3.0),
        trans(withdrawal = 0.0, deposit = 4.0),
        trans(withdrawal = 0.0, deposit = 5.0))

      Transaction.sum(Vector.empty[Transaction]) should be(BigDecimal.apply(0.0))
      Transaction.sum(ts) should be(BigDecimal(15.0))
    }

    "have withdrawls sum correctly" in {
      val ts = Vector(
        trans(withdrawal = 10.0, deposit = 0.0),
        trans(withdrawal = 2.0, deposit = 0.0),
        trans(withdrawal = 0.0, deposit = 2.0),
        trans(withdrawal = 0.0, deposit = 3.0),
        trans(withdrawal = 0.0, deposit = 4.0),
        trans(withdrawal = 0.0, deposit = 5.0))

      Transaction.sum(ts) should be(BigDecimal(2.0))
    }

    "allow you to change the category" in {
      val ts = Vector(trans(withdrawal = 0.0, deposit = 1.0))
      val major = MajorCategory("loans")
      val minor = MinorCategory("student")
      val c = Category(major, minor)

      val changed = Transaction.recategorize(c, ts)
      changed.head.category.major should be(major)
      changed.head.category.minor should be(minor)
    }

    "have the ability to mark transactions as 'cleared'" in {
      val t1 = trans(withdrawal = 0.0, deposit = 1.0)
      val t2 = trans(withdrawal = 0.0, deposit = 2.0)
      val state = Vector(t2, t1)

      val expected = Vector(t2, t1.copy(cleared = Cleared(true)))
      val result = Transaction.toggleCleared(Vector(t1), Vector(t1))

      result should be(Vector(t1.copy(cleared = Cleared(true))))
      Transaction.toggleCleared(Vector(t1), state) should be(expected)
    }
  }
}
