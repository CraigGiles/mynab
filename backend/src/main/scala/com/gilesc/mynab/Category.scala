package com.gilesc.mynab
package category

import com.gilesc.commons._
import com.gilesc.mynab.transaction.Transaction
import com.gilesc.mynab.account._

trait CategoryModule { self: Prepending with Removing =>
  type CategoryList = Vector[Category]

  def renameCategory(before: Category, after: Category, accts: AccountGroup): AccountGroup = {
    def rename(transactions: Vector[Transaction]): Vector[Transaction] = {
      transactions.map { t =>
        if (t.category == before) t.copy(category = after) else t
      }
    }

    // loop through each account in the account group
    // for each account, map over the transaction list
    // foreach transaction in the transaction list
    //    if we need to rename the category, rename it
    val newAccounts = accts.accounts map(t => t.copy(t.name, rename(t.transactions)))

    accts.copy(accounts = newAccounts)
  }
}

object Category extends CategoryModule with Prepending with Removing {
}

case class MajorCategory(value: String) extends AnyVal
case class MinorCategory(value: String) extends AnyVal
case class Category(major: MajorCategory, minor: MinorCategory)
