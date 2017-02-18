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
  with AccountServiceModule {

  def create = AccountService.create(InMemoryAccountRepository, NullLoggingModule)

  "Creating a new account service" should {

    "convert an account details object to an Account object" in {
      val name = "visa"
      val details = AccountDetails(name, Banking.toString, "groupname")
      AccountService.convert(details) should be(
        Right(Account(Banking, name, Vector.empty[Transaction])))
    }

    "persist the account object" in {
      val name = "visa"
      val details = AccountDetails(name, Banking.toString, "groupname")
      val result = create(details)
      result should be(Success(Account(Banking, name, Vector.empty[Transaction])))
    }
  }
}
