package com.gilesc.commons.validation

import cats._
import cats.implicits._

object StringValidation {
  val lengthIsGreaterThan:
    Int => String => Either[InvalidLengthError, String] = {
      length => value =>
        if (value.length <= length) Left(InvalidLengthError(value))
        else Right(value)
  }

  val nonEmpty: String => Either[InvalidLengthError, String] = {
    lengthIsGreaterThan(0)(_)
  }
}
