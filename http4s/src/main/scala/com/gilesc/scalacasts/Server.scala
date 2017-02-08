package com.gilesc

import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server._
import scalaz.concurrent.Task

object BlazeExample extends ServerApp {
  override def server(args: List[String]): Task[Server] = {
    BlazeBuilder
      .bindHttp(8080)
      .mountService(HelloWorld.service, "/")
      .start
  }
}
