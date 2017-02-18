package com.gilesc.mynab
package account

case class AccountDetails(accountName: String, accountType: String, groupName: String)

sealed trait AddAccountResult
final case class Success(account: Account) extends AddAccountResult
final case class Failure(message: String) extends AddAccountResult

trait AccountService {
}

object AccountService extends AccountService {

  def addNewAccount(details: AccountDetails): AddAccountResult = {
    val accountE = for {
      t <- AccountType(details.accountType)
      n <- AccountName(details.accountName)
      g <- AccountName(details.groupName)
    } yield Account.create(t, n)

    accountE match {
      case Left(s) => Failure(s.toString)
      case Right(a) => Success(a)
    }
  }
}

object Main extends App {
  val details = AccountDetails("My Account Name", "Banking", "No Group Name")
  val account = AccountService.addNewAccount(details)

  println(account)
}
