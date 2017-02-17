package com.gilesc.commons

package object validation {

  sealed trait ValidationError

  // String Validation
  case object InvalidLengthError extends ValidationError

}
