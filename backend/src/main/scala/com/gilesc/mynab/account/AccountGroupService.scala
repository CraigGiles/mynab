package com.gilesc
package mynab
package account

import com.gilesc.mynab.persistence.account._

object AccountGroupService {
  def createWithPersistence(name: AccountName): Either[String, AccountGroup] =
    create(AccountGroupRepository.create)(name)

  def create(save: AccountName => Either[AccountGroupPersistenceError, AccountGroupId])
    (name: AccountName): Either[String, AccountGroup] = {

    save(name) match {
      case Right(id) => Right(AccountGroup.create(id, name))
      case Left(DuplicateAccountGroupId) => Left(DuplicateAccountGroupId.toString)
    }
  }
}


