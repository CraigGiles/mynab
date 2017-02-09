package com.gilesc.mynab

import com.gilesc.mynab.Transaction._

trait AccountModule {
  def prependTransaction: (Transaction, List[Transaction]) => List[Transaction]
}

object AccountDomain {
  import com.gilesc.mynab.Transaction._

  case class AccountName(value: String) extends AnyVal

  sealed trait AccountType
  final case object Banking extends AccountType
  final case object Loan extends AccountType
  final case object Investment extends AccountType
  final case object Retirement extends AccountType

  trait Account {
    def accountType: AccountType
  }

  case class BankingAccount(name: AccountName, transactions: List[Transaction])
    extends Account { val accountType = Banking }

  case class LoanAccount(name: AccountName, transactions: List[Transaction])
    extends Account { val accountType = Loan }

  case class InvestmentAccount(name: AccountName, transactions: List[Transaction])
    extends Account { val accountType = Investment }

  case class RetirementAccount(name: AccountName, transactions: List[Transaction])
    extends Account { val accountType = Retirement }
}

object Account extends AccountModule {
  import AccountDomain._
  import com.gilesc.mynab.Transaction._

  val prependTransaction: (Transaction, List[Transaction]) => List[Transaction] =
    (t, s) => t :: s
}
