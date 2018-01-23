package com.gilesc

import java.util.UUID

package object mynab {
  case class CategoryName(value: String) extends AnyVal

  case class CategoryGroupId(value: Long) extends AnyVal
  case class CategoryGroup(id: CategoryGroupId, name: CategoryName)
  case class CategoryGroupContext(value: CategoryName)

  case class CategoryId(value: Long) extends AnyVal
  case class Category(id: CategoryId, group: CategoryGroup, name: CategoryName)
  case class CategoryContext(group: CategoryGroup, name: CategoryName)

//  // user
//  case class UserId(value: UUID) extends AnyVal
//
//  // account
//  case class AccountId(value: UUID) extends AnyVal
//  case class AccountName(value: String) extends AnyVal
//  case class Account(id: AccountId, userId: UserId, name: AccountName, transactions: Vector[Transaction])
//
//  case class AccountContext(userId: UserId, name: AccountName)
//
//  // category
//  case class MajorCategory(value: String) extends AnyVal
//  case class MinorCategory(value: String) extends AnyVal
//  case class Category(major: MajorCategory, minor: MinorCategory)
//
//  // transaction
//  import java.time.{LocalDate => Date}
//
//  case class TransactionId(value: UUID) extends AnyVal
//  case class Payee(value: String) extends AnyVal
//  case class Memo(value: String) extends AnyVal
//  case class Amount(value: BigDecimal) extends AnyVal
//  case class Cleared(value: Boolean) extends AnyVal
//
//  case class Transaction(id: TransactionId, date: Date, payee: Payee,
//    category: Category, memo: Memo, withdrawal: Amount, deposit: Amount,
//    cleared: Cleared)
//
//  case class TransactionContext(date: Date, payee: Payee,
//    category: Category, memo: Memo, withdrawal: Amount, deposit: Amount,
//    cleared: Cleared)
//
//  object Transaction {
//    def fromCtx(id: TransactionId, ctx: TransactionContext): Transaction =
//      Transaction(id, ctx.date, ctx.payee, ctx.category, ctx.memo,
//        ctx.withdrawal, ctx.deposit, ctx.cleared)
//  }
}
