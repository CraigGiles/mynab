package com.gilesc
package endpoints

import com.twitter.finagle.http.Status
import io.finch._
import io.finch.circe._
import io.circe.generic.auto._
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.prop.Checkers
import java.nio.charset.StandardCharsets

import io.finch.oauth2._

class UserEndpointSpec extends FlatSpec
  with Matchers
  with Checkers {

  import com.gilesc.endpoint.UserEndpoint
  import com.gilesc.endpoint.UserEndpoint._

  // behavior of "UserEndpoint - POST"
  // it should "get a user" in {
  //   // val data = arbitraryTransactionData.arbitrary.sample.get
  //   // val input = Input.post(s"/${TransactionEndpoint.path}")
  //   //   .withBody[Application.Json](data, Some(StandardCharsets.UTF_8))

  //   // val res = TransactionEndpoint.postTransaction(input)
  //   // res.awaitOutputUnsafe().map(_.status) shouldBe(Some(Status.Created))
  //   // res.awaitValueUnsafe().isDefined shouldBe(true)

  //   // val Some(transaction) = res.awaitValueUnsafe()
  //   // transaction.date shouldBe (data.date)
  //   // transaction.payee shouldBe (data.payee)
  //   // transaction.category.major shouldBe (data.category.major)
  //   // transaction.category.minor shouldBe (data.category.minor)
  //   // transaction.memo shouldBe (data.memo)
  //   // transaction.withdrawal shouldBe (data.withdrawal)
  //   // transaction.deposit shouldBe (data.deposit)

  //   // Todo.get(todo.id).isDefined === true
  // }

  // // http GET :8081/users/current access_token=='AT-5b0e7e3b-943f-479f-beab-7814814d0315'
  // behavior of "UserEndpoint - UNPROTECTED"
  // it should "get an unprotected user" in {
  //   // val hello = Vector.empty[TransactionData]
  //   val input = Input.get(s"/${UserEndpoint.endpoint}/unprotected")
  //   val result = UserEndpoint.getUnprotected(input)
  //   val Some(usr) = result.awaitValueUnsafe()
  //   usr shouldBe(UnprotectedUser("unprotected"))
  // }

  // behavior of "UserEndpoint - TOKENS"
  // it should "do something cool" in {
  //   val input = Input.post(s"/${UserEndpoint.endpoint}/auth")
  //     // .withBody[Application.Json]("grant_type==password", Some(StandardCharsets.UTF_8))

  //   val res = UserEndpoint.tokens(input)
  //   println("TOKENS: " + res.awaitValueUnsafe())
  //   // res.awaitOutputUnsafe().map(_.status) shouldBe(Some(Status.Created))
  //   // res.awaitValueUnsafe().isDefined shouldBe(true)

  //   // val Some(transaction) = res.awaitValueUnsafe()
  // }
  behavior of "the token-generating endpoint"
  it should "give an access token with the password grant type" in {
    val input = Input.post("/users/auth")
      .withForm(
        "grant_type" -> "password",
        "username" -> "user_name",
        "password" -> "user_password",
        "client_id" -> "user_id")

      UserEndpoint.tokens(input).awaitValueUnsafe().map(_.tokenType) shouldBe Some("Bearer")
  }

  it should "give an access token with the client credentials grant type" in {
    val input = Input.post("/users/auth")
      .withForm("grant_type" -> "client_credentials")
      .withHeaders("Authorization" -> "Basic dXNlcl9pZDp1c2VyX3NlY3JldA==")

      UserEndpoint.tokens(input).awaitValueUnsafe().map(_.tokenType) shouldBe Some("Bearer")
  }

  it should "give an access token with the auth code grant type" in {
    import com.gilesc.mynab.MynabSettings
    val input = Input.post("/users/auth")
      .withForm(
        "grant_type" -> "authorization_code",
        "code" -> "user_auth_code",
        "client_id" -> MynabSettings.clientId)

      val result = UserEndpoint.tokens(input)
      println(result.awaitValueUnsafe())
      result.awaitValueUnsafe().map(_.tokenType) shouldBe Some("Bearer")
  }

  it should "give back bad request if we omit the password for the password grant type" in {
    val input = Input.post("/users/auth")
      .withForm(
        "grant_type" -> "password",
        "username" -> "user_name",
        "client_id" -> "user_id")

      UserEndpoint.tokens(input).awaitOutputUnsafe().map(_.status) shouldBe Some(Status.BadRequest)
  }

  it should "give back nothing for other verbs" in {
    val input = Input.get("/users/auth")
      .withForm("grant_type" -> "authorization_code", "code" -> "code", "client_id" -> "id")

      UserEndpoint.tokens(input).awaitValueUnsafe() shouldBe None
  }

  behavior of "the authorized endpoint"

  it should "work if the access token is a valid one" in {
    val input = Input.post("/users/auth")
      .withForm("grant_type" -> "client_credentials")
      .withHeaders("Authorization" -> "Basic dXNlcl9pZDp1c2VyX3NlY3JldA==")

      val authdUser = UserEndpoint.tokens(input).awaitValueUnsafe()
        .map(_.accessToken).flatMap(t =>
            UserEndpoint.getCurrentUser(Input.get("/users/current").withForm("access_token" -> t)).awaitValueUnsafe()
            )

        authdUser shouldBe Some(OAuthUser("user", "John Smith"))
  }

  it should "be unauthorized when using an invalid access token" in {
    val input = Input.get("/users/current")
      .withForm("access_token" -> "at-5b0e7e3b-943f-479f-beab-7814814d0315")

      UserEndpoint.getCurrentUser(input).awaitOutputUnsafe().map(_.status) shouldBe Some(Status.Unauthorized)
  }

  it should "give back nothing for other verbs" in {
    val input = Input.post("/users/current")
      .withForm("access_token" -> "at-5b0e7e3b-943f-479f-beab-7814814d0315")

      UserEndpoint.getCurrentUser(input).awaitValueUnsafe() shouldBe None
  }

  behavior of "the unprotected users endpoint"

  it should "give back the unprotected user" in {
    UserEndpoint.getUnprotected(Input.get("/users/unprotected")).awaitValueUnsafe() shouldBe
    Some(UnprotectedUser("unprotected"))
  }

  it should "give back nothing for other verbs" in {
    UserEndpoint.getUnprotected(Input.post("/users/unprotected")).awaitValueUnsafe() shouldBe None
  }
}
