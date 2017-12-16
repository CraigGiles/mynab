package com.gilesc
package mynab
package authentication

import cats.data.Validated
import cats.data.Validated.{Invalid, Valid}

import com.gilesc.mynab.user._

case class Salt(value: String) extends AnyVal
case class RawPassword(value: String) extends AnyVal
case class HashedPassword(hash: String, salt: Salt)

object PasswordHashing {
  import org.mindrot.jbcrypt.BCrypt

  val gensalt: Salt = Salt(BCrypt.gensalt())

  def hash(raw: RawPassword, salt: Salt): HashedPassword =
    HashedPassword(BCrypt.hashpw(raw.value, salt.value), salt)

  def hash(raw: RawPassword): HashedPassword = hash(raw, gensalt)

  def verify(plain: RawPassword, hashed: HashedPassword): Boolean =
    BCrypt.checkpw(plain.value, hashed.hash)
}

