package com.gilesc
package mynab
package account

import com.gilesc.mynab.persistence.account._
import cats.implicits._

object AccountGroupService {
  sealed trait FindBy
  final case class FindByName(value: String) extends FindBy
  type AccountGroupResult[T] = Either[AccountGroupPersistenceError, T]

  def createWithPersistence(name: AccountName): Either[String, AccountGroup] =
    create(AccountGroupRepository.create)(name)

  def create(save: AccountName => Either[AccountGroupPersistenceError, AccountGroupId])
    (name: AccountName): Either[String, AccountGroup] = {

    save(name) match {
      case Right(id) => Right(AccountGroup.create(id, name))
      case Left(InvalidAccountNameLength(name)) => Left(s"$name is not a valid account name")
      case Left(DuplicateAccountGroupId) => Left(DuplicateAccountGroupId.toString)
    }
  }

  def find(read: AccountName => AccountGroupResult[AccountGroupRow])
    (by: FindBy): Either[String, AccountGroup] = {

      by match {
        case FindByName(name) => findByName(read)(FindByName(name))
      }
  }

  def findByName(read: AccountName => Either[AccountGroupPersistenceError, AccountGroupRow])
    (name: FindByName): Either[String, AccountGroup] = {
    import com.gilesc.commons.validation._

    AccountName(name.value) flatMap { n =>
      read(n) map { r => parseRow(r) }
    } leftMap {
      case InvalidLengthError(n) => s"$n is not a valid account name"
      case InvalidAccountNameLength(n) => s"$n is not a valid account name"
    }
  }

  def parseRow(row: AccountGroupRow): AccountGroup = {
    AccountGroup(row.id, row.name, Vector.empty[Account])
  }
}


