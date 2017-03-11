package com.gilesc
package mynab

import argonaut.Argonaut._
import org.http4s._
import org.http4s.dsl._
import org.http4s.argonaut._
import com.gilesc.mynab.account._
import cats.implicits._
import com.gilesc.mynab.transaction.TransactionService

object InMemoryGroups {
  var groups = Vector.empty[AccountGroup]

  def save(name: AccountName): Either[AccountGroupPersistenceError, AccountGroupId] = {
    val id = groups.length
    val group = AccountGroup(AccountGroupId(id.toLong), name, Vector.empty[Account])
    groups = groups :+ group
    Right(group.id)
  }

}

object HelloWorld {
  val mynab = HttpService {
    case GET -> Root / "account-group" / name =>
      AccountName("Budget Accounts") match {
        case Right(accountName) =>
          val saveResult = InMemoryGroups.save(accountName)
          println(saveResult)

          Ok(jSingleObject("created", jString(name)))

        case Left(_) =>
          Ok(jSingleObject("error", jString("Unable to create account name")))
      }

    case GET -> root / "transaction" / id =>
      AccountService.find()
  }

  val service = HttpService {
    case GET -> Root / "hello" / name =>
      Ok(jSingleObject("message", jString(s"Hello, ${name}")))
  }
}
