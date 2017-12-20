package com.gilesc
package mynab

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.util.Try
import scala.io.StdIn
import scala.concurrent.ExecutionContext.Implicits.global

import com.typesafe.scalalogging.StrictLogging

import com.gilesc.mynab.route.RouteList

object WebServer extends StrictLogging {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem(AppSettings.actorSystemName)
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val host = AppSettings.host
    val port = AppSettings.port
    val bindingFuture = Http().bindAndHandle(RouteList.routes, host, port)

    println(s"""Server online at http://$host:$port/""")

    // let it run until user presses return
    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
