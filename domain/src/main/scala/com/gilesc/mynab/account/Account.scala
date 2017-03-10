package com.gilesc.mynab
package account

import cats.data.State
import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction.Transaction

case class AccountId(value: Long) extends AnyVal

trait Account {
  def id: AccountId
  def name: AccountName
  def accountType: AccountType
  def transactions: Vector[Transaction]

  def copy(id: AccountId = id,
    name: AccountName = name,
    transactions: Vector[Transaction] = transactions) =

    this match {
      case BankingAccount(_, _, _) => BankingAccount(id, name, transactions)
      case LoanAccount(_, _, _) => LoanAccount(id, name, transactions)
      case InvestmentAccount(_, _, _) => InvestmentAccount(id, name, transactions)
      case RetirementAccount(_, _, _) => RetirementAccount(id, name, transactions)
  }
}

object Account extends AccountModule with Prepending with Removing {
  def apply(id: AccountId, account: AccountType, name: AccountName,
    transactions: Vector[Transaction]): Account = account match {
      case Banking => BankingAccount(id, name, transactions)
      case Loan => LoanAccount(id, name, transactions)
      case Investment => InvestmentAccount(id, name, transactions)
      case Retirement => RetirementAccount(id, name, transactions)
  }

  val create: (AccountId, AccountType, AccountName) => Account = (i, t, n) =>
    Account(i, t, n, Vector.empty[Transaction])

  val add: Transaction => State[Account, Unit] = trans =>
    State[Account, Unit] { acc =>
      (acc.copy(acc.id, acc.name, prepend(trans, acc.transactions)), ())
    }
}

