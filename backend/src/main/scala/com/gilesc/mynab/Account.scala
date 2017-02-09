package com.gilesc.mynab

object Account {
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
}
