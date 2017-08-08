package com.gilesc
package mynab
package route

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import akka.actor.ActorSystem
// import akka.event.NoLogging
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.StatusCodes
// import akka.http.scaladsl.model.headers.{BasicHttpCredentials, HttpChallenge}
// import akka.http.scaladsl.server.AuthenticationFailedRejection
// import akka.http.scaladsl.server.AuthenticationFailedRejection.{CredentialsMissing, CredentialsRejected}
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import akka.testkit.TestActorRef

import scala.concurrent.Future
import scala.concurrent.duration._
import akka.http.scaladsl.model.HttpRequest
import com.gilesc.mynab.route.AccountRoute._
import com.gilesc.mynab.route.PresentationData._
import scala.concurrent.Await
import scala.concurrent.duration._
import io.circe.generic.auto._
import io.circe.syntax._

class AccountRouteSpec extends FlatSpec
  with Matchers
  with ScalatestRouteTest
  with ScalaFutures {

  implicit def default(implicit system: ActorSystem) = RouteTestTimeout(5 seconds)
  override def testConfigSource = "akka.loglevel = DEBUG"

  def createNewAccount(data: AccountResource): AccountData = {
    var response: Option[AccountData] = None

    Post("/accounts", data.asJson) ~> AccountRoute.postAccountsRoute ~> check {
      status shouldBe StatusCodes.Created
      response = Option(responseAs[AccountData])
    }

    response.get
  }

  "account routes" should "get me an account" in {
    val u = java.util.UUID.randomUUID.toString()
    val resource = AccountResource("helloworld")
    val data = createNewAccount(resource)

    Get("/accounts/" + data.id) ~> AccountRoute.getAccountsRoute ~> check {
      status shouldBe StatusCodes.OK
      contentType shouldBe ContentTypes.`application/json`
      responseAs[AccountData] shouldBe data
    }
  }

}
