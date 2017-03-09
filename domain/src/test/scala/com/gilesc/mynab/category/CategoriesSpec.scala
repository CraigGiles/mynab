package com.gilesc.mynab
package category

import com.gilesc.mynab.account._

class CategorySpec extends TestCase
  with MockTransactionCreation
  with MockAccountCreation
  with TestCaseHelpers {

  import Category._

  "Categories" should {
    "be able to be added to the system" in {
      val loans = "loans"
      val student = "student"
      val mortgage = "mortgage"

      val c1 = Category(MajorCategory(loans), MinorCategory(mortgage))
      val c2 = Category(MajorCategory(loans), MinorCategory(student))

      val group = Vector(c2, c1)

      val state01 = prepend(c1, Vector.empty[Category])
      state01 should be(Vector(c1))

      prepend(c2, state01) should be(group)
    }
  }

  "Renaming a category" should {
    "rename all categories within the transaction system" in {
      val changeme = "changeme"
      val loans = "loans"
      val student = "student"
      val mortgage = "mortgage"

      val transaction = trans(minorCategory = changeme)
      val stay = trans(minorCategory = mortgage)
      val checkingAcc = banking("chase", Vector(transaction, stay))

      val before = Category(MajorCategory(loans), MinorCategory(changeme))
      val after = Category(MajorCategory(loans), MinorCategory(student))
      val group = AccountGroup(1L, "budget accounts", Vector(checkingAcc))

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
