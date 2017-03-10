package com.gilesc.mynab.transaction

import cats.implicits._
import com.gilesc.mynab.account.{Account, AccountId}

sealed trait TransactionPersistenceError
case object DuplicateTransactionId extends TransactionPersistenceError
final case class InvalidAccountId(id: AccountId) extends TransactionPersistenceError

object TransactionService {
  def create(
    save: TransactionContext => Either[TransactionPersistenceError, TransactionId],
    find: AccountId => Option[Account])
    (ctx: TransactionContext): Either[TransactionPersistenceError, Account] = {

    for {
      account <- Either.fromOption(find(ctx.accountId) , InvalidAccountId(ctx.accountId))
      id <- save(ctx)
    } yield account.copy(transactions = account.transactions :+
      Transaction(id, ctx.date, ctx.payee, ctx.category, ctx.memo, ctx.withdrawal, ctx.deposit, ctx.cleared))
  }
}
