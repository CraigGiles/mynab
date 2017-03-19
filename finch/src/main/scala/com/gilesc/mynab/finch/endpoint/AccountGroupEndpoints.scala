package com.gilesc
package mynab
package finch
package endpoint

import com.gilesc.mynab.account._
import com.gilesc.mynab.finch.InMemoryRepos._
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

object AccountGroupEndpoints {
  val path = "account-group"
  def getGroup: Endpoint[String] = get(path) {
    Ok(GroupsRepo.groups.toString)
  }

  def postGroup: Endpoint[String] = get(path :: string) { name: String =>
    val result = AccountName(name) match {
      case Right(accountName) =>
        GroupsRepo.save(accountName) match {
          case Right(id) => GroupsRepo.groups.toString
          case Left(error) => s"ERROR: $error"
        }

      case Left(_) => "ERROR"
    }

    Created(result)
  }

  val api: Service[Request, Response] = (
    AccountGroupEndpoints.postGroup :+: 
    AccountGroupEndpoints.getGroup
  ).handle({
    case e => NotFound(new Exception(e.toString))
  }).toServiceAs[Text.Plain]

}


