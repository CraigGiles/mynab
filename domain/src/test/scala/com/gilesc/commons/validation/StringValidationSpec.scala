package com.gilesc.commons.validation

import com.gilesc.mynab.TestCase
import cats._
import cats.implicits._

class StringValidationSpec extends TestCase {
  "String Validation" should "validate the length of a string" in {
      val greaterThanOne = StringValidation.lengthIsGreaterThan(1)
      val testme = "1"
      val valid = "12"
      val empty = ""
      greaterThanOne(testme) should be(Left(InvalidLengthError(testme)))
      greaterThanOne(empty) should be(Left(InvalidLengthError(empty)))
      greaterThanOne(valid) should be(Right(valid))
    }

    it should "validate non-empty strings" in {
      val something = "s"
      val empty = ""
      StringValidation.nonEmpty(empty) should be(Left(InvalidLengthError(empty)))
      StringValidation.nonEmpty(something) should be(Right(something))
    }

    it should "let you chain validations" in {
      val greaterThanOne = StringValidation.lengthIsGreaterThan(1)
      val valid = "12"

      val something = for {
        a <- greaterThanOne(valid)
        b <- StringValidation.nonEmpty(a)
      } yield b

      something should be(Right(valid))
    }

}
