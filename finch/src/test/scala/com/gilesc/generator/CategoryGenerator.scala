package com.gilesc
package generator

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

trait CategoryGenerator {
  import com.gilesc.mynab.PresentationData._

  def genCategoryData: Gen[CategoryData] = for {
    major <- Gen.alphaStr
    minor <- Gen.alphaStr
  } yield CategoryData(major, minor)
  implicit def arbitraryCategoryData: Arbitrary[CategoryData] = Arbitrary(genCategoryData)
}

