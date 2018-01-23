package com.gilesc
package mynab
package testkit
package generator

import org.scalacheck.Gen

import java.util.UUID
import scala.util.Random

trait AccountGenerator { self: TransactionGenerator =>
//  def generateAccountId: Stream[AccountId] = Stream.cons(AccountId(UUID.randomUUID), generateAccountId)
//  def generateUserId: Stream[UserId] = Stream.cons(UserId(UUID.randomUUID), generateUserId)
//  def generateAccountName: Stream[AccountName] = Stream.cons(AccountName(Gen.alphaStr.sample.get), generateAccountName)
//  def generateAccount(
//    userId: UserId = generateUserId.head,
//    numTransactions: Int = Random.nextInt(5)
//  ): Stream[Account] = {
//
//    val acct =
//    Account(
//      generateAccountId.head,
//      userId,
//      generateAccountName.head,
//      generateTransaction.take(numTransactions).toVector
//    )
//
//    Stream.cons(acct, generateAccount(userId, numTransactions))
//  }
}
