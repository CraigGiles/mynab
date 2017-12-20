package com.gilesc
package mynab
package route

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import akka.http.scaladsl.model._
import akka.http.scaladsl.server._

trait ExceptionHandling {
  val exceptionHandler = ExceptionHandler {
    case _: Exception =>
      extractUri { uri =>
        // TODO: Replace with log
        println(s"Request to $uri could not be handled normally")
        complete(
          HttpResponse(
            StatusCodes.InternalServerError, entity = "Internal Server Error"))
      }
  }
}

object RouteList extends ExceptionHandling {
  val routes: Route = handleExceptions(exceptionHandler) {
    concat(
    )
  }

}
