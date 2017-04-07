package com.gilesc
package mynab

import java.time.LocalDate

import com.gilesc.mynab.account._
import com.gilesc.mynab.category._
import com.gilesc.mynab.transaction._
import com.gilesc.mynab.user._

import com.gilesc.mynab.persistence.account.AccountGroupRepository.CreateContext
import com.gilesc.mynab.persistence.account._
import AccountGroupService.FindByName

import java.time.ZonedDateTime

class PersistingAccountGroupSpec extends TestCase
  with MockAccountCreation {

  val id = 1L
  val validName = "newaccountgroup"
  val invalidName = "r"
  val time = ZonedDateTime.now()

  "Giving the service an AccountName" should "persist the account group" in {
    val userId = UserId(id)
    val expected = AccountGroup.create(id, validName)
    def mockSave(ctx: CreateContext) = Right(AccountGroupId(id))

    AccountGroupService.create(mockSave)(userId, validName) should be(Right(expected))
  }

  it should "find an account group given a proper account name" in {
    def mockFind(an: AccountName): Either[AccountGroupPersistenceError, Option[AccountGroupRow]] =
      Right(Option(AccountGroupRow(id, an, time, time, None)))

    AccountGroupService.find(mockFind)(FindByName(validName)) should be(
      Right(Some(AccountGroup.create(1L, validName))))
  }

  it should "never call the mock function if an invalid account name is provided" in {
    val expected = s"$invalidName is not a valid account name"
    def mockFind(an: AccountName): Either[AccountGroupPersistenceError, Option[AccountGroupRow]] =
      sys.error("Somehow passed the AccountName restriction")

    AccountGroupService.find(mockFind)(FindByName(invalidName)) should be(Left(expected))
  }

  it should "fail gracefully if the database contains an invalid name" in {
    val expected = s"n is not a valid account name"

    def mockFind(an: AccountName): Either[AccountGroupPersistenceError, Option[AccountGroupRow]] =
      Left(InvalidAccountNameLength("n"))

    AccountGroupService.find(mockFind)(FindByName(validName)) should be(Left(expected))

  }
}
