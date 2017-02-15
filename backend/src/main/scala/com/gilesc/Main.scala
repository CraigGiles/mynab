package com.gilesc

import java.time.LocalDate

import com.gilesc.commons._
import com.gilesc.mynab.account._
import com.gilesc.mynab.transaction._

object Main extends App with Prepending {

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

  implicit def str2AccountName(str: String): AccountName = AccountName(str)

  val ebmud = Transaction(LocalDate.now(), "East Bay Municipal District", "Housing", "Water", "", 105.26, 0.0, cleared = false)
  val income = Transaction(LocalDate.now(), "Credit Karma", "Income", "This Month", "", 0, 3742.56, cleared = false)
  val chaseChecking = BankingAccount("Chase Checking", Vector(income, ebmud))

  val trans1 = Transaction(LocalDate.now(), "Frontpoint Security", "Housing", "Security", "", 45.00, 0.0, cleared = false)
  val trans2 = Transaction(LocalDate.now(), "Netflix", "Lifestyle", "Movies", "", 9.99, 0.0, cleared = false)
  val trans3 = Transaction(LocalDate.now(), "Comcast", "Lifestyle", "Internet", "", 65.00, 0.0, cleared = false)
  val trans4 = Transaction(LocalDate.now(), "T-Mobile", "Lifestyle", "Cell Phone", "", 111.12, 0.0, cleared = false)

  val cc_state = Vector(trans1, trans2, trans3, trans4)
  val chaseVisaAmazon = BankingAccount("Chase Amazon CC", cc_state)

  val group = AccountGroup("Budget Accounts", Vector(chaseChecking, chaseVisaAmazon))

  prettyPrint(group)

}
