package com.gilesc.mynab
package account

import cats.data.State
import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction.Transaction

sealed trait AccountType
case object Banking extends AccountType
case object Loan extends AccountType
case object Investment extends AccountType
case object Retirement extends AccountType

trait Account {
  def name: AccountName
  def accountType: AccountType
  def transactions: Vector[Transaction]

  def copy(name: AccountName, transactions: Vector[Transaction]) = this match {
    case BankingAccount(_, _) => BankingAccount(name, transactions)
    case LoanAccount(_, _) => LoanAccount(name, transactions)
    case InvestmentAccount(_, _) => InvestmentAccount(name, transactions)
    case RetirementAccount(_, _) => RetirementAccount(name, transactions)
  }
}

object Account extends AccountModule with Prepending with Removing {
  def apply(account: AccountType, name: AccountName, transactions: Vector[Transaction]): Account = {
    account match {
      case Banking => BankingAccount(name, transactions)
      case Loan => LoanAccount(name, transactions)
      case Investment => InvestmentAccount(name, transactions)
      case Retirement => RetirementAccount(name, transactions)
    }
  }

  val create: (AccountType, AccountName) => Account = (t, n) =>
    Account(t, n, Vector.empty[Transaction])

  val add: Transaction => State[Account, Unit] = trans =>
    State[Account, Unit] { acc =>
      (acc.copy(acc.name, prepend(trans, acc.transactions)), ())
    }
}

case class AccountName(value: String) extends AnyVal
case class BankingAccount(name: AccountName, transactions: Vector[Transaction]) extends Account { val accountType = Banking }
case class LoanAccount(name: AccountName, transactions: Vector[Transaction]) extends Account { val accountType = Loan }
case class InvestmentAccount(name: AccountName, transactions: Vector[Transaction]) extends Account { val accountType = Investment }
case class RetirementAccount(name: AccountName, transactions: Vector[Transaction]) extends Account { val accountType = Retirement }
