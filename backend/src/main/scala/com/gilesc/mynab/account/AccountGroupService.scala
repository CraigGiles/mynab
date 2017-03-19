package com.gilesc
package mynab
package account

sealed trait AccountGroupPersistenceError
case object DuplicateAccountGroupId extends AccountGroupPersistenceError

object AccountGroupService {
  def create(save: AccountName => Either[AccountGroupPersistenceError, AccountGroupId])
    (name: AccountName): Either[String, AccountGroup] = {

    save(name) match {
      case Right(id) => Right(AccountGroup.create(id, name))
      case Left(DuplicateAccountGroupId) => Left(DuplicateAccountGroupId.toString)
    }
  }
}


