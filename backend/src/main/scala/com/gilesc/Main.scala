package com.gilesc

import java.time.LocalDate

import com.gilesc.commons._
import com.gilesc.mynab.account._
import com.gilesc.mynab.category._
import com.gilesc.mynab.transaction._

object Main extends App with Prepending {
  def trans(date: LocalDate = LocalDate.now(),
            payee: String = "Me",
            majorCategory: String = "loans",
            minorCategory: String = "studen",
            memo: String = "loans",
            withdrawal: Double = 0.0,
            deposit: Double = 0.0,
            cleared: Boolean = false): Transaction = {

    Transaction(LocalDate.now(),
      Payee(payee),
      Category.apply(MajorCategory(majorCategory), MinorCategory(minorCategory)),
      Memo(memo),
      Amount(BigDecimal(withdrawal)),
      Amount(BigDecimal(deposit)),
      Cleared(cleared))
  }

  implicit def str2AccountName(str: String): AccountName = AccountName(str)
  def makeVector[T](items: T*): Vector[T] = items.foldRight(Vector.empty[T])( (t, l) => l :+ t)

  val ebmud = trans(LocalDate.now(), "East Bay Municipal District", "Housing", "Water", "", 105.26, 0.0, cleared = false)
  val income = trans(LocalDate.now(), "Credit Karma", "Income", "This Month", "", 0, 3742.56, cleared = false)
  val state0 = prepend(income, Vector.empty[Transaction])
  val state1 = prepend(ebmud, state0)
  val chaseChecking = BankingAccount("Chase Checking", state1)

  val trans1 = trans(LocalDate.now(), "Frontpoint Security", "Housing", "Security", "", 45.00, 0.0, cleared = false)
  val trans2 = trans(LocalDate.now(), "Netflix", "Lifestyle", "Movies", "", 9.99, 0.0, cleared = false)
  val trans3 = trans(LocalDate.now(), "Comcast", "Lifestyle", "Internet", "", 65.00, 0.0, cleared = false)
  val trans4 = trans(LocalDate.now(), "T-Mobile", "Lifestyle", "Cell Phone", "", 111.12, 0.0, cleared = false)

//  val cc_state1 = prepend(trans1, Vector.empty[Transaction])
//  val cc_state2 = prepend(trans2, cc_state1)
//  val cc_state3 = prepend(trans3, cc_state2)
//  val cc_state4 = prepend(trans4, cc_state3)
  val cc_state = makeVector(trans1, trans2, trans3, trans4)
  val chaseVisaAmazon = BankingAccount("Chase Amazon CC", cc_state.toVector)

  val group = AccountGroup("Budget Accounts", Vector(chaseChecking, chaseVisaAmazon))

  def prettyPrint(group: AccountGroup) = {
    import com.gilesc.mynab.account.AccountGroup._
    import com.gilesc.mynab.transaction.Transaction._

    println()
    println("----------------------------- ")
    println(s" ${group.name.value}  -  ${sumAllAccounts(group.accounts)}")
    println("----------------------------- ")
    group.accounts.foreach { account =>
      println(s" > ${account.name.value}  - $$${sumTransactions(account.transactions)}")
    }
    println("----------------------------- ")
    println()

  }

  prettyPrint(group)

}
