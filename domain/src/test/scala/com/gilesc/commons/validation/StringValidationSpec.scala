package com.gilesc.commons.validation

import com.gilesc.mynab.TestCase

class StringValidationSpec extends TestCase {
  "String Validation" should {
    "validate the length of a string" in {
      val greaterThanOne = StringValidation.lengthIsGreaterThan(1)
      val testme = "1"
      val valid = "12"
      val empty = ""
      greaterThanOne(testme) should be(Left(InvalidLengthError))
      greaterThanOne(empty) should be(Left(InvalidLengthError))
      greaterThanOne(valid) should be(Right(valid))
    }

    "validate non-empty strings" in {
      val something = "s"
      val empty = ""
      StringValidation.nonEmpty(empty) should be(Left(InvalidLengthError))
      StringValidation.nonEmpty(something) should be(Right(something))
    }

    "let you chain validations" in {
      val greaterThanOne = StringValidation.lengthIsGreaterThan(1)
      val valid = "12"

      val something = for {
        a <- greaterThanOne(valid)
        b <- StringValidation.nonEmpty(a)
      } yield b

      something should be(Right(valid))
    }
  }

}
