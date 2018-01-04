package com.gilesc
package mynab
package route

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import akka.http.scaladsl.model._
import akka.http.scaladsl.server._


import akka.http.scaladsl.model.{ ContentType, ContentTypes, HttpCharset, HttpEntity }
import akka.http.scaladsl.model.StatusCodes.Success
import akka.http.scaladsl.model.{ HttpHeader, HttpResponse }
import akka.http.scaladsl.model.headers.{ `Cache-Control` }
import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.model.headers.CacheDirectives._

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
  val testme =
    ((pathPrefix("assets" / Remaining) & respondWithHeader(`Cache-Control`(`no-cache`)))) { file =>
      // optionally compresses the response with Gzip or Deflate
      // if the client accepts compressed responses
      getFromResource("public/" + file)
    }
  val routes: Route = handleExceptions(exceptionHandler) {
    concat(testme)
  }

}
