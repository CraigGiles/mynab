package com.gilesc
package mynab
package account

import com.gilesc.mynab.persistence.account._
import com.gilesc.mynab.persistence.account.AccountGroupRepository.CreateContext
import com.gilesc.mynab.user.UserId
import cats._
import cats.implicits._

object AccountGroupService {
  sealed trait FindBy
  final case class FindByName(value: String) extends FindBy

  def createWithPersistence(id: UserId, name: AccountName): Either[String, AccountGroup] =
    create(AccountGroupRepository.create)(id, name)

  def findWithPersistence(by: FindBy): Either[String, Option[AccountGroup]] =
    find(AccountGroupRepository.read)(by)

  def create(save: CreateContext => Either[AccountGroupPersistenceError, AccountGroupId])
    (id: UserId, name: AccountName): Either[String, AccountGroup] = {

    save(CreateContext(id, name)) match {
      case Right(id) => Right(AccountGroup.create(id, name))
      case Left(InvalidAccountNameLength(name)) => Left(s"$name is not a valid account name")
      case Left(DuplicateAccountGroupId) => Left(DuplicateAccountGroupId.toString)
    }
  }

  def find(read: AccountName => Either[AccountGroupPersistenceError, Option[AccountGroupRow]])
    (by: FindBy): Either[String, Option[AccountGroup]] = {

      by match {
        case FindByName(name) => findByName(read)(FindByName(name))
      }
  }

  def findByName(read: AccountName => Either[AccountGroupPersistenceError, Option[AccountGroupRow]])
    (name: FindByName): Either[String, Option[AccountGroup]] = {
    import com.gilesc.commons.validation._

    val lifted = Monad[Option].lift(parseRow)
    AccountName(name.value) flatMap { n =>
      read(n) map { r => lifted(r) }
    } leftMap {
      case InvalidLengthError(n) => s"$n is not a valid account name"
      case InvalidAccountNameLength(n) => s"$n is not a valid account name"
    }
  }

  def parseRow: AccountGroupRow => AccountGroup = { row =>
    AccountGroup(row.id, row.name, Vector.empty[Account])
  }
}


