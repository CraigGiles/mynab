package com.gilesc

import java.time.LocalDate

import com.gilesc.commons._
import com.gilesc.mynab.account._
import com.gilesc.mynab.transaction._

object Main extends App
  with Prepending
  with Removing
  with TransactionModule
  with AccountGroupModule
  with AccountModule {

  // for the account name implicit
  import AccountImplicits._

  def prettyPrint(group: AccountGroup) = {

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

  val checkingTransactions = for {
    a <- addTransactionState(Transaction("East Bay Municipal District", "Housing", "Water", "", 105.26, 0.0))
    b <- addTransactionState(Transaction("Credit Karma", "Income", "This Month", "", 0, 3742.56))
  } yield ()
  val chaseChecking = checkingTransactions.runS(newAccount(Banking, "Chase Checking")).value

  val visaTransactions = for {
    _ <- addTransactionState(Transaction("Frontpoint Security", "Housing", "Security", "", 45.00, 0.0))
    _ <- addTransactionState(Transaction("Netflix", "Lifestyle", "Movies", "", 9.99, 0.0))
    _ <- addTransactionState(Transaction("Comcast", "Lifestyle", "Internet", "", 65.00, 0.0))
    _ <- addTransactionState(Transaction("T-Mobile", "Lifestyle", "Cell Phone", "", 111.12, 0.0))
  } yield ()
  val chaseVisaAmazon = visaTransactions.runS(newAccount(Banking, "Chase Amazon CC")).value

  val accounts = for {
    _ <- addAccount(chaseChecking)
    _ <- addAccount(chaseVisaAmazon)
  } yield ()
  val group = accounts.runS(newAccountGroup("Budget Accounts")).value

  prettyPrint(group)

}
