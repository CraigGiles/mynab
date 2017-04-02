package com.gilesc.commons

package object validation {

  sealed trait ValidationError

  // String Validation
  case class InvalidLengthError(value: String) extends ValidationError

}
