package com.gilesc
package mynab
package finch
package endpoint

import com.gilesc.mynab.account._
import com.gilesc.mynab.finch.InMemoryRepos._
import com.gilesc.mynab.finch.presenter.DataFactories
import io.circe.syntax._
import io.finch._

import io.circe.generic.auto._

object AccountGroupEndpoints {
  val path = "account-group"
  def getGroup: Endpoint[String] = get(path) {
    Ok(GroupsRepo.groups.toString)
  }

  def postGroup: Endpoint[String] = get(path :: string) { name: String =>
    val result = AccountName(name) match {
      case Right(accountName) =>
        GroupsRepo.save(accountName) match {
          case Right(id) =>
            val account = Account.create(AccountId(id.value), Banking, accountName)
            val mapped = GroupsRepo.groups map { group =>
              group.copy(accounts = Vector(account))
            }

            (mapped map DataFactories.accountGroup).asJson
          case Left(error) => Vector(error.asJson)
        }

      case Left(error) => Vector(error.asJson)
    }

    Created(result.toString)
  }

}


