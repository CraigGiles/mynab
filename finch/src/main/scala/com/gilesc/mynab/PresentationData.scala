package com.gilesc
package mynab

object PresentationData {
  import com.gilesc.mynab.account._
  import com.gilesc.mynab.transaction._
  import com.gilesc.mynab.category._

  case class CategoryData(major: String, minor: String)
  case class TransactionData(id: String, date: String, payee: String,
    category: CategoryData, memo: String, withdrawal: Double, deposit: Double,
    cleared: Boolean)
  case class AccountData(id: String, userId: String, name: String, transactions: Vector[TransactionData])

  val fromCategory: Category => CategoryData = { (category: Category) =>
    CategoryData(
      category.major.value,
      category.minor.value)
  }

  val fromTransaction: Transaction =>  TransactionData = { (t: Transaction) =>
    TransactionData(
      t.id.toString,
      t.date.toString,
      t.payee.value,
      fromCategory(t.category),
      t.memo.value,
      t.withdrawal.value.toDouble,
      t.deposit.value.toDouble,
      t.cleared.value)
  }

  val fromAccount: Account => AccountData = { (account: Account) =>
    val transactions = account.transactions map fromTransaction
    AccountData(
      account.id.value.toString,
      account.userId.value.toString,
      account.name.value,
      transactions)
  }
}

