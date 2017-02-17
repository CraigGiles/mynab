package com.gilesc.commons.validation

sealed trait ValidationError
case object InvalidLengthError extends ValidationError

object StringValidation {
  val lengthIsGreaterThan:
    Int => String => Either[InvalidLengthError.type, String] = {
      length => value =>
        if (value.length <= length) Left(InvalidLengthError)
        else Right(value)
  }

  val nonEmpty: String => Either[InvalidLengthError.type, String] = {
    lengthIsGreaterThan(1)(_)
  }
}
