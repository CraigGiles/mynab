package com.gilesc
package mynab

import java.time.LocalDate

import com.gilesc.mynab.account._
import com.gilesc.mynab.category._
import com.gilesc.mynab.transaction._

import com.gilesc.mynab.persistence.account._
import AccountGroupService.FindByName

import java.time.ZonedDateTime

class PersistingAccountGroupSpec extends TestCase
  with MockAccountCreation {

  val id = 1L
  val validName = "newaccountgroup"
  val invalidName = "er"
  val time = ZonedDateTime.now()

  "Giving the service an AccountName" should "persist the account group" in {
    val expected = AccountGroup.create(id, validName)
    def mockSave(name: AccountName) = Right(AccountGroupId(id))

    AccountGroupService.create(mockSave)(validName) should be(Right(expected))
  }

  it should "find an account group given a proper account name" in {
    def mockFind(an: AccountName): Either[AccountGroupPersistenceError, AccountGroupRow] =
      Right(AccountGroupRow(id, an, time, time, None))

    AccountGroupService.find(mockFind)(FindByName(validName)) should be(
      Right(AccountGroup.create(1L, validName)))
  }

  it should "give you sensible error messages" in {
    val expected = s"$invalidName is not a valid account name"

    def mockFind(an: AccountName): Either[AccountGroupPersistenceError, AccountGroupRow] =
      Left(InvalidAccountNameLength(invalidName))

    AccountGroupService.find(mockFind)(FindByName(invalidName)) should be(Left(expected))
  }

  it should "fail gracefully if the database contains an invalid name" in {
    val expected = s"n is not a valid account name"

    def mockFind(an: AccountName): Either[AccountGroupPersistenceError, AccountGroupRow] =
      Left(InvalidAccountNameLength("n"))

    AccountGroupService.find(mockFind)(FindByName(validName)) should be(Left(expected))

  }
}
