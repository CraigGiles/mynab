package com.gilesc
package generator

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

trait TransactionGenerator { self: CategoryGenerator with DateGenerator =>
  import com.gilesc.mynab.PresentationData._

  import java.util.UUID
  def genTransactionData: Gen[TransactionData] = for {
    payee <- Gen.alphaStr
    date <- arbitraryLocalDateString.arbitrary
    category <- arbitraryCategoryData.arbitrary
    memo <- Gen.alphaStr
    withdrawal <- Gen.choose(Double.MinValue, Double.MaxValue)
    deposit <- Gen.choose(Double.MinValue, Double.MaxValue)
    cleared <- Gen.oneOf(true, false)
  } yield TransactionData(UUID.randomUUID().toString, date, payee, category, memo, withdrawal, deposit, cleared)
  implicit def arbitraryTransactionData: Arbitrary[TransactionData] = Arbitrary(genTransactionData)
}

