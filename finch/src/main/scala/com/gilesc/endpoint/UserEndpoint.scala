package com.gilesc
package endpoint

import com.twitter.finagle._
import com.twitter.finagle.oauth2.{AuthInfo, GrantHandlerResult}
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

/**
  * {{{
  *   $ http POST :8081/users/auth Authorization:'OAuth dXNlcl9pZDp1c2VyX3NlY3JldA=='\
  *     grant_type==client_credentials
  *
  *   $ http POST :8081/users/auth grant_type==password username==user_name\
  *     password==user_password client_id==user_id
  *
  *   $ http POST :8081/users/auth grant_type==authorization_code code==user_auth_code client_id==user_id
  *
  *   $ http GET :8081/users/current access_token=='AT-5b0e7e3b-943f-479f-beab-7814814d0315'
  *
  *   $ http POST :8081/users/auth client_id==user_id grant_type==refresh_token\
  *     refresh_token=='RT-7e1bbf43-e7ba-4a8a-a38e-baf62ce3ceae'
  *
  *   $ http GET :8081/users/unprotected
  * }}}
  */
object UserEndpoint {
  case class UnprotectedUser(name: String)
  import io.finch.oauth2._

  def getCurrentUser: Endpoint[OAuthUser] = get("users" :: "current" :: authorize(InMemoryDataHandler)) {
    ai: AuthInfo[OAuthUser] => Ok(ai.user)
  }

  def tokens: Endpoint[GrantHandlerResult] =
    post("users" :: "auth" :: issueAccessToken(InMemoryDataHandler))

  def getUnprotected: Endpoint[UnprotectedUser] = get("users" :: "unprotected") {
    Ok(UnprotectedUser("unprotected"))
  }

}

// object TestAuth {
//   // import com.twitter.finagle.http.{Request, Response}
//   // import com.twitter.finagle.{Http, Service}
//   // import com.twitter.util.Await
//   // import com.twitter.finagle._
//   // import com.twitter.finagle.oauth2.{AuthInfo, GrantHandlerResult}
//   // import com.twitter.util.Await
//   // import io.circe.generic.auto._
//   // import io.finch._
//   // import io.finch.circe._

//   case class Auth(username: String)
//   val auth = headerOption("Auth").withDefault("anon").as[Auth].mapOutput { a =>
//     if (a.username == "foo") Ok(a)
//     else Unauthorized(new Exception("wrong credentials"))
//   }

//   def testme() = {
//     val endpoint = get("foo" :: auth).map(_.username)
//     val service = endpoint.toService

//     service(Request("/foo")).get

//     val req = Request("/foo")
//     req.headerMap.set("Auth", "foo")
//     service(req).get

//     service(req).get.contentString
//   }
// }
