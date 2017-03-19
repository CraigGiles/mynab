package com.gilesc
package mynab
package finch
package endpoint

import com.gilesc.mynab.account.{AccountName, Banking}
import com.gilesc.mynab.finch.InMemoryRepos.AccountsRepo
import com.gilesc.mynab.finch.presenter.CircePresenter
import com.gilesc.mynab.transaction.Transaction
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import io.finch.{Endpoint, Text, _}
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

object AccountEndpoints {
  val path = "account"

  def getAccount: Endpoint[String] = get(path) {
    Ok(AccountsRepo.accounts.toString)
  }

  def postAccount: Endpoint[Json] = get(path :: string) { name: String =>
    val result = AccountName(name) match {
      case Right(accountName) =>
        AccountsRepo.save(accountName, Banking) match {
          case Right(id) => id.asJson
            val account = AccountsRepo.accounts.find(_.id == id).head
            val t = Transaction(1L, "Payee", "Major", "Minor", "Memo", 100, 0)
            CircePresenter.present(account.copy(transactions = Vector(t)))
          case Left(error) => error.asJson
        }

      case Left(error) => error.asJson
    }

    Created(result)
  }

  val endpoints = AccountEndpoints.getAccount :+:
      AccountEndpoints.postAccount

  val api: Service[Request, Response] = endpoints.handle({
    case e => NotFound(new Exception(e.toString))
  }).toServiceAs[Text.Plain]
}
