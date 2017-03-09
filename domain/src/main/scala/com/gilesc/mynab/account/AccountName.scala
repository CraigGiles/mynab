package com.gilesc.mynab
package account

import com.gilesc.commons.validation.{StringValidation, ValidationError}

import cats._
import cats.implicits._

class AccountName private (val value: String) extends AnyVal {
  override def toString: String = value
}

object AccountName {
  val MIN_NAME_LENGTH = 1

  def apply(value: String): Either[ValidationError, AccountName] =
    StringValidation.lengthIsGreaterThan(MIN_NAME_LENGTH)(value) map(new AccountName(_))
}
