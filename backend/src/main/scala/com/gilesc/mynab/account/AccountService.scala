package com.gilesc.mynab
package account

import cats.implicits._

sealed trait AccountPersistenceError
case object DuplicateAccountId extends AccountPersistenceError
final case class InvalidAccountGroupId(id: AccountGroupId) extends AccountPersistenceError

case class AccountContext(group: AccountGroupId, name: AccountName, accType: AccountType)

object AccountService {

  def create(
    save: AccountContext => Either[AccountPersistenceError, AccountId],
    find: AccountGroupId => Option[AccountGroup])
    (ctx: AccountContext): Either[AccountPersistenceError, AccountGroup] = {

    for {
      group <- Either.fromOption(find(ctx.group) , InvalidAccountGroupId(ctx.group))
      accId <- save(ctx)
    } yield group.copy(accounts = group.accounts :+ Account.create(accId, ctx.accType, ctx.name))
  }
}
