package com.gilesc
package mynab
package account

import com.gilesc.mynab.transaction.Transaction

import java.util.UUID

case class AccountId(value: UUID) extends AnyVal
case class UserId(value: UUID) extends AnyVal
case class AccountName(value: String) extends AnyVal

case class AccountContext(userId: UserId, name: AccountName)
case class Account(id: AccountId, userId: UserId, name: AccountName, transactions: Vector[Transaction])
import scala.collection.mutable

sealed trait FindBy
object FindBy {
  final case class Id[T](id: T) extends FindBy
}

object Accounts {
  private[this] val db: mutable.Map[UUID, Account] = mutable.Map.empty[UUID, Account]

  def findById(find: FindBy.Id[AccountId]): Option[Account] = synchronized { db.get(find.id.value) }
  def list(): List[Account] = synchronized { db.values.toList }
  def create(ctx: AccountContext): Account = synchronized {
    val id = AccountId(UUID.randomUUID())
    val account = Account(id, ctx.userId, ctx.name, Vector.empty[Transaction])
    db += (id.value -> account)
    account
  }
  def delete(id: UUID): Unit = synchronized { db -= id }
}

case class AccountNotFound(id: UUID) extends Exception {
  override def getMessage: String = s"Account(${id.toString}) not found."
}
