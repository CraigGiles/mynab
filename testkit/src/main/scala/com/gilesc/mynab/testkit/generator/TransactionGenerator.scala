package com.gilesc
package mynab
package testkit
package generator

import org.scalacheck.Gen

import java.util.UUID
import java.time.LocalDate
import java.time.OffsetDateTime

trait TransactionGenerator {
  def generateTransactionId: Stream[TransactionId] = Stream.cons(TransactionId(UUID.randomUUID), generateTransactionId)
  def generateDate: Stream[LocalDate] = {
    val year = Gen.choose(1800, OffsetDateTime.now().getYear()).sample.get
    val month = Gen.choose(1, 12).sample.get
    val maxday = if (month == 2) 28 else 30
    val day = Gen.choose(1, maxday).sample.get
    Stream.cons(
      LocalDate.of(year, month, day),
      generateDate)
  }
  def generatePayee: Stream[Payee] = Stream.cons(Payee(Gen.alphaStr.sample.get), generatePayee)
  def generateCategory: Stream[Category] = Stream.cons(
    Category(
      MajorCategory(Gen.alphaStr.sample.get),
      MinorCategory(Gen.alphaStr.sample.get)),
    generateCategory)

  def generateMemo: Stream[Memo] = Stream.cons(Memo(Gen.alphaStr.sample.get), generateMemo)
  def generateAmount: Stream[Amount] = Stream.cons(Amount(BigDecimal(Gen.choose(Long.MinValue, Long.MaxValue).sample.get)), generateAmount)
  def generateCleared: Stream[Cleared] = Stream.cons(Cleared(
    Gen.oneOf(true, false).sample.get), generateCleared)

  def generateTransaction: Stream[Transaction] = Stream.cons(
    Transaction(
      generateTransactionId.head,
      generateDate.head,
      generatePayee.head,
      generateCategory.head,
      generateMemo.head,
      generateAmount.filter(_.value > 0).head,
      generateAmount.filter(_.value > 0).head,
      generateCleared.head),
    generateTransaction)
}

