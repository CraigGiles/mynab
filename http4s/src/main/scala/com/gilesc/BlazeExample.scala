package com.gilesc
package mynab

import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.{Server, ServerApp}

import scalaz.concurrent.Task

object BlazeExample extends ServerApp {
  override def server(args: List[String]): Task[Server] = {
    BlazeBuilder
      .bindHttp(8080)
      .mountService(HelloWorld.mynab, "/")
      .start
  }
}
