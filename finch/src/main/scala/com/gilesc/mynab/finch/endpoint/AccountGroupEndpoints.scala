package com.gilesc
package mynab
package finch
package endpoint

import com.gilesc.mynab.account._
import com.gilesc.mynab.finch.InMemoryRepos.GroupsRepo
import com.gilesc.mynab.finch.presenter.{AccountGroupData, PresentationData}

import io.finch.{Created, Endpoint, post, _}
import io.finch.circe._
import io.circe.generic.auto._

object AccountGroupEndpoints {
  case class AccountGroupContext(name: String)
  val path = "account-group"

  def getGroup: Endpoint[Vector[AccountGroupData]] = get(path) {
    Ok(GroupsRepo.groups map PresentationData.accountGroup)
  }

  def postGroup: Endpoint[AccountGroupData] =
    post(path :: jsonBody[AccountGroupContext]) { s: AccountGroupContext =>
    AccountName(s.name) match {
      case Right(n) => persist(n)
      case Left(string) => BadRequest(new IllegalArgumentException(string.toString))
    }
  }

  def persist(name: AccountName): Output[AccountGroupData] = {
    AccountGroupService.createWithPersistence(name) match {
      case Left(string) => BadRequest(new IllegalArgumentException(string))
      case Right(group) => Created(PresentationData.accountGroup(group))
    }
  }
}


