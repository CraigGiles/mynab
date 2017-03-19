package com.gilesc
package mynab
package finch
package presenter

import com.gilesc.mynab.account.Account
import io.circe.Json

object CirceAccountPresenter extends Presenter[Account, Json] {
  import io.circe._
  import io.circe.generic.auto._
  import io.circe.syntax._
  import com.gilesc.mynab.transaction.Transaction

  case class CategoryJson(major: String, minor: String)
  case class TransactionsJson(id: Long, date: String, payee: String, category: CategoryJson, memo: String, withdrawal: BigDecimal, deposit: BigDecimal, cleared: Boolean)
  case class AccountJson(id: Long, name: String, `type`: String, transactions: Vector[TransactionsJson])

  def present(account: Account): Json = {
    def presentTransaction(t: Transaction) = {
      TransactionsJson(t.id.value,
        t.date.toString,
        t.payee.value,
        CategoryJson(t.category.major.value, t.category.minor.value),
        t.memo.value,
        t.withdrawal.value,
        t.deposit.value,
        t.cleared.value)
    }

    val t = account.transactions.map(presentTransaction)

    AccountJson(
      account.id.value,
      account.name.value,
      account.accountType.toString,
      t
    ).asJson
  }

}
