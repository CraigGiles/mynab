package com.gilesc

import java.time.LocalDate

import com.gilesc.commons._
import com.gilesc.mynab.account._
import com.gilesc.mynab.transaction._

object Main extends App
  with Prepending
  with Removing {

  // for the account name implicit
  import AccountImplicits._

  def prettyPrint(group: AccountGroup) = {

    println()
    println("----------------------------- ")
    println(s" ${group.name.value}  -  ${AccountGroup.sum(group.accounts)}")
    println("----------------------------- ")
    group.accounts.foreach { account =>
      println(s" > ${account.name.value}  - $$${Transaction.sum(account.transactions)}")
    }
    println("----------------------------- ")
    println()

  }

  val checkingTransactions = for {
    a <- Account.add(Transaction("East Bay Municipal District", "Housing", "Water", "", 105.26, 0.0))
    b <- Account.add(Transaction("Credit Karma", "Income", "This Month", "", 0, 3742.56))
  } yield ()
  val chaseChecking = checkingTransactions.runS(Account.create(Banking, "Chase Checking")).value

  val visaTransactions = for {
    _ <- Account.add(Transaction("Frontpoint Security", "Housing", "Security", "", 45.00, 0.0))
    _ <- Account.add(Transaction("Netflix", "Lifestyle", "Movies", "", 9.99, 0.0))
    _ <- Account.add(Transaction("Comcast", "Lifestyle", "Internet", "", 65.00, 0.0))
    _ <- Account.add(Transaction("T-Mobile", "Lifestyle", "Cell Phone", "", 111.12, 0.0))
  } yield ()
  val chaseVisaAmazon = visaTransactions.runS(Account.create(Banking, "Chase Amazon CC")).value

  val accounts = for {
    _ <- AccountGroup.add(chaseChecking)
    _ <- AccountGroup.add(chaseVisaAmazon)
  } yield ()
  val group = accounts.runS(AccountGroup.create("Budget Accounts")).value

  prettyPrint(group)

}
