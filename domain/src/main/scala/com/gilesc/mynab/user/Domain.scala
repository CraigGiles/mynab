package com.gilesc
package mynab
package user

import com.gilesc.mynab.authentication._
import com.gilesc.mynab.UniqueId

case class UserId(value: Long) extends AnyVal
case class Username(value: String) extends AnyVal
case class Email(value: String) extends AnyVal

case class User(
  id: UserId,
  username: Username,
  email: Email,
  password: HashedPassword)
