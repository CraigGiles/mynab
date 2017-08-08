package com.gilesc
package generator

import org.scalacheck.Gen
import org.scalacheck.Arbitrary

trait AccountGenerator { self: CategoryGenerator with DateGenerator =>
  import com.gilesc.endpoint.AccountEndpoint._

  def genAccountResource: Gen[AccountResource] = for {
    name <- Gen.alphaStr
  } yield AccountResource(name)

  implicit def arbitraryAccountResource: Arbitrary[AccountResource] =
    Arbitrary(genAccountResource)
}

