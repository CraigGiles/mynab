package com.gilesc
package endpoint

import io.finch.circe._
import io.finch.{Endpoint, _}
import io.circe.generic.auto._

import com.gilesc.mynab.PresentationData
import com.gilesc.mynab.PresentationData._
import com.gilesc.mynab.account._


import java.util.UUID

object AccountEndpoint {
  val accounts = "accounts"
  case class AccountResource(name: String)

  def contextBody: Endpoint[AccountResource] = jsonBody[AccountResource]

  def postAccount: Endpoint[AccountData] = post(accounts :: contextBody) { t: AccountResource =>
      val accountCtx = AccountContext(UserId(UUID.randomUUID()), AccountName(t.name))
      val account = Accounts.create(accountCtx)
      val data = PresentationData.fromAccount(account)

      Created(data)
  }

  def getAccount: Endpoint[AccountData] = get(accounts :: uuid) { id: UUID =>
    val findby = FindBy.Id(AccountId(id))
    val account = Accounts.findById(findby)

    account match {
      case Some(a) => Ok(PresentationData.fromAccount(a))
      case None => throw AccountNotFound(id)
    }
  }
}
