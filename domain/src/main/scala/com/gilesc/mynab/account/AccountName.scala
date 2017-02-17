package com.gilesc.mynab
package account

import com.gilesc.commons.validation.{StringValidation, ValidationError}

class AccountName private (val value: String) extends AnyVal

object AccountName {
  val AccountNameLength = 8
  def apply(value: String): Either[ValidationError, AccountName] =
    StringValidation.lengthIsGreaterThan(AccountNameLength)(value) map(new AccountName(_))
}
