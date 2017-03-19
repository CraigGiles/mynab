package com.gilesc
package mynab
package finch

import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import com.twitter.util.Await
import io.finch.{Text, _}

import com.gilesc.mynab.finch.endpoint._

object WebServer {
  // Endpoints
  val api: Service[Request, Response] = (
//      AccountGroupEndpoints.postGroup :+:
//      AccountGroupEndpoints.getGroup :+:
      AccountEndpoints.getAccount :+:
      AccountEndpoints.postAccount
    ).handle({
    case e => NotFound(new Exception(e.toString))
  }).toServiceAs[Text.Plain]

  // Service Stuff
  def shutdown(): Unit = {}

  def main(args: Array[String]): Unit = {
    val server = Http.server.serve(":8080", api)
    Await.ready(server)
    shutdown()
  }
}
