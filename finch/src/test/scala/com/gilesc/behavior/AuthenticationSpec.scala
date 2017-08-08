package com.gilesc
package behavior

import com.twitter.finagle.http.Status
import org.scalatest.{FlatSpec, Matchers}
import io.finch._
import com.gilesc.endpoint.UserEndpoint
import com.gilesc.endpoint.UserEndpoint._

abstract class TestCase extends FlatSpec with Matchers

class AuthenticationSpec extends TestCase {

  behavior of "the authentication system"
  it should "allow a user to login" in {
    val input = Input.post("/users/auth")
      .withForm(
        "grant_type" -> "password",
        "username" -> "myusername",
        "password" -> "mypassword",
        "client_id" -> "mynab_web")

      val res = UserEndpoint.tokens(input)
      val result = res.awaitValueUnsafe()

      result.map(_.tokenType) shouldBe Some("Bearer")
  }

  it should "disallow a user to use the wrong password" in {
    val input = Input.post("/users/auth")
      .withForm(
        "grant_type" -> "password",
        "username" -> "myusername",
        "password" -> "wrongpassword",
        "client_id" -> "mynab_web")

      val res = UserEndpoint.tokens(input)
      val result = res.awaitOutputUnsafe()

      result.map(_.status) shouldBe Some(Status.Unauthorized)
  }

//   it should "handle request" in {
//     import com.twitter.finagle.oauth2._
//     val password = new Password(new MockClientCredentialFetcher())
//     val request = AuthorizationRequest(Map(), Map("username" -> Seq("user"), "password" -> Seq("pass"), "scope" -> Seq("all")))
//     val grantHandlerResult = Await.result(password.handleRequest(request, new MockDataHandler() {
//       override def findUser(username: String, password: String): Future[Option[MockUser]] =
//         Future.value(Some(MockUser(10000, "username")))

//       override def createAccessToken(authInfo: AuthInfo[MockUser]): Future[AccessToken] =
//         Future.value(AccessToken("token1", Some("refreshToken1"), Some("all"), Some(3600), new java.util.Date()))
//     }))
//     grantHandlerResult.tokenType should be ("Bearer")
//     grantHandlerResult.accessToken should be ("token1")
//     grantHandlerResult.expiresIn should be (Some(3600))
//     grantHandlerResult.refreshToken should be (Some("refreshToken1"))
//     grantHandlerResult.scope should be (Some("all"))
//   }
}
