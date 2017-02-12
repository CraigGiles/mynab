package com.gilesc.mynab
package category

import com.gilesc.mynab.account._
import com.gilesc.mynab.transaction.Transaction

class CategorySpec extends TestCase
  with MockTransactionCreation
  with MockAccountCreation {

  import Category._

  "Renaming a category" should {
    "rename all categories within the transaction system" in {
      val changeme = "changeme"
      val student = "student"

      val transaction = trans(minorCategory = changeme)
      val checkingAcc = banking("chase", List(transaction))

      val o = Category(MajorCategory("loans"), MinorCategory(changeme))
      val n = Category(MajorCategory("loans"), MinorCategory(student))
      val acc = AccountGroup(AccountName("budget accounts"), List(checkingAcc))

      val newAccountGroup = renameCategory(o, n, acc)

      newAccountGroup.transactions.foreach { t =>
        t.category.minor.value should be(student)
      }

    }
  }
}
