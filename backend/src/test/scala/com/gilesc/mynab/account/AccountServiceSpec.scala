package com.gilesc.mynab
package account

import com.gilesc.mynab.logging.LoggingModule
import com.gilesc.mynab.repository.InMemoryAccountRepository
import com.gilesc.mynab.transaction.Transaction

object NullLoggingModule extends LoggingModule {
  override def debug: (String) => Unit = str => ()
  override def info: (String) => Unit = str => ()
  override def warn: (String) => Unit = str => ()
  override def error: (String) => Unit = str => ()
}

class AccountServiceSpec extends TestCase
  with TestCaseHelpers
  with AccountServiceModule
  with MockTransactionCreation {

  def find = AccountService.find(InMemoryAccountRepository.find, NullLoggingModule)
  def create = AccountService.create(InMemoryAccountRepository.save, NullLoggingModule)
//  def rename = AccountService.rename(InMemoryAccountRepository, NullLoggingModule)

  "Creating a new account" should {
    "convert an account details object to an Account object" in {
      val name = "visa"
      val id = 1L
      val details = AccountDetails(id, name, Banking.toString, "groupname")
      AccountService.convert(details) should be(
        Right(Account(id, Banking, name, Vector.empty[Transaction])))
    }

    "not let you convert an invalid name into an account" in {
      val name = ""
      val id = 1L
      val details = AccountDetails(id, name, Banking.toString, "groupname")
      AccountService.convert(details) should be(Left("InvalidLengthError"))
    }

    "persist the account object" in {
      val name = "visa"
      val id = 1L
      val details = AccountDetails(id, name, Banking.toString, "groupname")
      val result = create(details)
      result should be(Success(Account(id, Banking, name, Vector.empty[Transaction])))
    }
  }

  "Finding an account" should {
    "attempt to find your account based on account name" in {
      val name = "visa"
      val id = 1L
      val details = FindDetails(name)
      val accDetails = AccountDetails(id, name, Banking.toString, "groupname")
      val result = AccountService.convert(accDetails)
      val account = result.toOption.get

      find(details) should be(Some(account))
    }
  }

//  "Renaming an account" should {
//    "allow you to rename the account" in {
//      val id = 1l
//      val newname = "My New Account"
//      val request = AccountRenameRequest(id, newname)
//      val result = rename(request)
//
//      result should be(Success(Account(id, Banking, newname, Vector.empty[Transaction])))
//    }
//  }
}
