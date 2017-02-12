package com.gilesc.mynab
package category

import com.gilesc.mynab.account._

class CategorySpec extends TestCase
  with MockTransactionCreation
  with MockAccountCreation {

  import Category._

  "Renaming a category" should {
    "rename all categories within the transaction system" in {
      val changeme = "changeme"
      val loans = "loans"
      val student = "student"
      val mortgage = "mortgage"

      val transaction = trans(minorCategory = changeme)
      val stay = trans(minorCategory = mortgage)
      val checkingAcc = banking("chase", List(transaction, stay))

      val before = Category(MajorCategory(loans), MinorCategory(changeme))
      val after = Category(MajorCategory(loans), MinorCategory(student))
      val group = AccountGroup(AccountName("budget accounts"), List(checkingAcc))

      val newAccountGroup = renameCategory(before, after, group)

      newAccountGroup.transactions.foreach { t =>
        t.category.minor.value shouldNot be(changeme)

        val one = t.category.minor.value == student
        val two = t.category.minor.value == mortgage

        // Assert that the minor category is either student or mortgage
        // Since major category is the same, we want to make sure that
        // only the proper minor categories get changed
        assert(one || two)
      }

    }
  }
}
