package com.gilesc
package mynab
package authentication

import cats.data.Validated
import cats.data.Validated.Invalid
import cats.data.Validated.Valid

object PasswordValidator {
  sealed trait PasswordError
  case object InvalidLengthError extends PasswordError

  implicit def validate: String => Validated[PasswordError, String] = { pw =>
    if (pw.length <= 7) Invalid(InvalidLengthError)
    else Valid(pw)
  }
}

class PasswordHashingSpec extends UnitSpec {

  behavior of "password hashing algorithm"
    import PasswordValidator._
    implicit def validate: String => Validated[PasswordError, String] =
      Valid(_)

    val plain = "mypassword"
    val raw = RawPassword(plain)
    val salt = Salt("""$2a$10$fztyjhMHpnXW4wn4/bnuB.""")
    val hashed = """$2a$10$fztyjhMHpnXW4wn4/bnuB.b.abfUR74lUrQa/tHcc.8AT1JGFbTWu"""

    it should "hash a password if given a salt" in {
      val getsalt: Salt = salt
      val raw = RawPassword(plain)
      val expected = HashedPassword(hashed, salt)

      PasswordHashing.hash(raw, getsalt) should be(expected)
    }

    it should "verify a password if given a HashedPasword" in {
      val hashedPw = HashedPassword(hashed, salt)

      PasswordHashing.verify(raw, hashedPw) should be(true)
    }

}
