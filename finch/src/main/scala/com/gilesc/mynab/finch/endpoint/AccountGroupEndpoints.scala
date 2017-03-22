package com.gilesc
package mynab
package finch
package endpoint


import com.gilesc.mynab.account._
import com.gilesc.mynab.finch.InMemoryRepos.GroupsRepo
import com.gilesc.mynab.finch.presenter.{AccountGroupData, DataFactories}
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Created, Endpoint, post, _}

object AccountGroupEndpoints {
  case class AccountGroupContext(name: String)

  val path = "account-group"
  def getGroup: Endpoint[Vector[AccountGroupData]] = get(path) {
    Ok(GroupsRepo.groups map DataFactories.accountGroup)
  }

  def postGroup: Endpoint[AccountGroupData] = post(path :: jsonBody[AccountGroupContext]) { s: AccountGroupContext =>
    val name = s.name

    AccountName(name) match {
      case Right(an) =>
        GroupsRepo.save(an) match {
          case Right(id) =>
            val account = Account.create(AccountId(id.value), Banking, an)
            val group = GroupsRepo.groups.find(_.id == id).get
            val newgroup = DataFactories.accountGroup(group.copy(accounts = group.accounts :+ account))

            Created(newgroup)

          case Left(persistenceError) =>
            BadRequest(new IllegalArgumentException(persistenceError.toString))
        }

      case Left(error) => BadRequest(new IllegalArgumentException(error.toString))
    }

  }
}


