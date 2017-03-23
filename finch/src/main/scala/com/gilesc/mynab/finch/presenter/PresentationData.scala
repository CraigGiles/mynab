package com.gilesc
package mynab
package finch
package presenter

import com.gilesc.mynab.account._
import com.gilesc.mynab.transaction.Transaction

case class CategoryData(major: String, minor: String)
case class TransactionData(id: Long, date: String, payee: String, category: CategoryData, memo: String, withdrawal: BigDecimal, deposit: BigDecimal, cleared: Boolean)
case class AccountData(id: Long, name: String, `type`: String, transactions: Vector[TransactionData])
case class AccountGroupData(id: Long, name: String, accounts: Vector[AccountData])

object PresentationData {
  def transaction(t: Transaction): TransactionData =
    TransactionData(t.id.value,
      t.date.toString,
      t.payee.value,
      CategoryData(t.category.major.value, t.category.minor.value),
      t.memo.value,
      t.withdrawal.value,
      t.deposit.value,
      t.cleared.value)

  def account(a: Account): AccountData = AccountData(
      a.id.value,
      a.name.value,
      a.accountType.toString,
      a.transactions.map(transaction))

  def accountGroup(g: AccountGroup): AccountGroupData = AccountGroupData(
      g.id.value,
      g.name.value,
      g.accounts.map(account)
    )
}

