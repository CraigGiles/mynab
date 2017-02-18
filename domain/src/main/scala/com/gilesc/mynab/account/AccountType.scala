package com.gilesc.mynab
package account

import com.gilesc.mynab.transaction.Transaction

sealed trait AccountType
case object Banking extends AccountType
case object Loan extends AccountType
case object Investment extends AccountType
case object Retirement extends AccountType

case class BankingAccount(name: AccountName, transactions: Vector[Transaction]) extends Account { val accountType = Banking }
case class LoanAccount(name: AccountName, transactions: Vector[Transaction]) extends Account { val accountType = Loan }
case class InvestmentAccount(name: AccountName, transactions: Vector[Transaction]) extends Account { val accountType = Investment }
case class RetirementAccount(name: AccountName, transactions: Vector[Transaction]) extends Account { val accountType = Retirement }

final case class InvalidAccountType(message: String) extends AnyVal {
  override def toString: String = message
}

object AccountType {
  def apply(value: String): Either[InvalidAccountType, AccountType] =
    value.trim.toLowerCase match {
      case "banking" => Right(Banking)
      case "loan" => Right(Loan)
      case "investment" => Right(Investment)
      case "retirement" => Right(Retirement)
      case _ => Left(InvalidAccountType(s"$value is an Account Type"))
    }
}
