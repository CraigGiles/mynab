package com.gilesc

import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.{Server, ServerApp}

import scalaz.concurrent.Task

/**
  * Created by craiggiles on 2/17/17.
  */
object BlazeExample extends ServerApp {
  override def server(args: List[String]): Task[Server] = {
    BlazeBuilder
      .bindHttp(8080)
      .mountService(HelloWorld.service, "/")
      .start
  }
}
