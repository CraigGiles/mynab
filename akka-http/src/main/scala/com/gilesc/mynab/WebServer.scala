package com.gilesc
package mynab

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.util.Try
import scala.io.StdIn
import scala.concurrent.ExecutionContext.Implicits.global

import com.softwaremill.session._
import com.softwaremill.session.CsrfDirectives._
import com.softwaremill.session.CsrfOptions._
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._

import com.typesafe.scalalogging.StrictLogging

object SessionRoutes extends StrictLogging {
  val sessionConfig = SessionConfig.default("really-long-string-that-needs-to-be-generated")
  implicit val sessionManager = new SessionManager[ExampleSession](sessionConfig)
  implicit val refreshTokenStorage = new InMemoryRefreshTokenStorage[ExampleSession] {
    def log(msg: String) = logger.info(msg)
  }

  def mySetSession(v: ExampleSession) = setSession(refreshable, usingCookies, v)
  val myRequiredSession = requiredSession(refreshable, usingCookies)
  val myInvalidateSession = invalidateSession(refreshable, usingCookies)

  val baseRoute =  path("") {
    redirect("/site/index.html", Found)
  }

  val doLoginPath = path("do_login") {
    post {
      entity(as[String]) { body =>
        logger.info(s"Logging in $body")

        mySetSession(ExampleSession(body)) {
          setNewCsrfToken(checkHeader) { ctx => ctx.complete("ok") }
        }
      }
    }
  }

  val doLogoutPath = path("do_logout") {
    post {
      myRequiredSession { session =>
        myInvalidateSession { ctx =>
          logger.info(s"Logging out $session")
          ctx.complete("ok")
        }
      }
    }
  }

  val currentLoginPath = path("current_login") {
    get {
      myRequiredSession { session => ctx =>
        logger.info("Current session: " + session)
        ctx.complete(session.username)
      }
    }
  }

  val apiRoute = pathPrefix("api") {
      doLoginPath ~
      doLogoutPath ~ // This should be protected and accessible only when logged in
      currentLoginPath  // This should be protected and accessible only when logged in
    }

  val siteRoute = pathPrefix("site") {
    getFromResourceDirectory("")
  }

  val routes = randomTokenCsrfProtection(checkHeader) {
    apiRoute ~
    siteRoute
  }
}

case class ExampleSession(username: String)
object ExampleSession {
  implicit def serializer: SessionSerializer[ExampleSession, String] =
    new SingleValueSessionSerializer(
      _.username,
      (username: String) => Try { ExampleSession(username) }
    )
}

object HelloWorldEndpoint {
  val getFoo = path("foo") {
    get {
      complete {
        HttpEntity(ContentTypes.`text/html(UTF-8)`,
          "<h1>Say hello to akka-http via foo</h1>")
      }
    }
  }

  val getHello = path("hello") {
    get {
      complete {
        HttpEntity(ContentTypes.`text/html(UTF-8)`,
          "<h1>Say hello to akka-http via hello</h1>")
      }
    }
  }
}

object WebServer extends StrictLogging {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem(MynabSettings.actorSystemName)
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val routes: Route = concat(
      HelloWorldEndpoint.getHello
      ,HelloWorldEndpoint.getFoo
      ,SessionRoutes.baseRoute
      ,SessionRoutes.routes
    )

    val host = MynabSettings.host
    val port = MynabSettings.port
    val bindingFuture = Http().bindAndHandle(routes, host, port)

    println(s"""
      Server online at http://localhost:8080/\n
      Press RETURN to stop...""")

    // let it run until user presses return
    StdIn.readLine()

    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
