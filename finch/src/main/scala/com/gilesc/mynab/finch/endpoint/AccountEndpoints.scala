package com.gilesc
package mynab
package finch
package endpoint

import com.gilesc.mynab.account._
import com.gilesc.mynab.finch.InMemoryRepos.AccountsRepo
import com.gilesc.mynab.finch.presenter.{AccountData, DataFactories}
import io.finch.{Endpoint, _}
import io.circe.generic.auto._
import io.finch.circe._

object AccountEndpoints {
  val path = "account"
  case class AccountContext(name: String)

  def getAccount: Endpoint[Vector[AccountData]] = get(path) {
    Ok(AccountsRepo.accounts map DataFactories.account)
  }

  def postAccount: Endpoint[AccountData] = post(path :: jsonBody[AccountContext]) { ctx: AccountContext =>
    val t = Banking // TODO: Remove

    AccountName(ctx.name) match {
      case Right(an) =>
        AccountsRepo.save(an, t) match {
          case Right(id) =>
            val account = Account.create(id, t, an)
            Created(DataFactories.account(account))
          case Left(error) =>
            BadRequest(new IllegalArgumentException(error.toString))
        }

      case Left(error) =>
        BadRequest(new IllegalArgumentException(error.toString))
    }
  }

}
