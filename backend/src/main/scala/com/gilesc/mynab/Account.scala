package com.gilesc.mynab
package account

import com.gilesc.commons.{Prepending, Removing}
import com.gilesc.mynab.transaction.{Cleared, Transaction}

trait AccountModule { self: Prepending with Removing =>
  type TransactionState = Vector[Transaction]

  def toggleCleared: (Vector[Transaction], TransactionState) => TransactionState
}

object Account extends AccountModule with Prepending with Removing {
  def toggleCleared: (Vector[Transaction], TransactionState) => TransactionState =
    (t, s) => s.flatMap { tr =>
      t.map { ti =>
        if (tr == ti) ti.copy(cleared = Cleared(!tr.cleared.value)) else tr
      }
    }
}

// Account Domain Objects
// ------------------------------------------------------------------------
case class AccountName(value: String) extends AnyVal

sealed trait AccountType
final case object Banking extends AccountType
final case object Loan extends AccountType
final case object Investment extends AccountType
final case object Retirement extends AccountType

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

case class BankingAccount(name: AccountName, transactions: Vector[Transaction])
  extends Account { val accountType = Banking }

case class LoanAccount(name: AccountName, transactions: Vector[Transaction])
  extends Account { val accountType = Loan }

case class InvestmentAccount(name: AccountName, transactions: Vector[Transaction])
  extends Account { val accountType = Investment }

case class RetirementAccount(name: AccountName, transactions: Vector[Transaction])
  extends Account { val accountType = Retirement }
