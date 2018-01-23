package com.gilesc
package arrow

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import cats.Monad
import cats.data.Kleisli

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import cats.implicits._

class ServiceSpec extends FlatSpec with Matchers {

  final class EchoService extends Service[Future, String, String] {
    override def run(req: String): Future[String] = Future.successful(req)
  }

  final class ToUpperService extends Service[Future, String, String] {
    override def run(req: String): Future[String] = Future.successful(req.toUpperCase)
  }

  final class AddEmphasisService extends Service[Future, String, String] {

    override def run(req: String): Future[String] = Future.successful(req.concat("!"))
  }

  final class RequestLogger[F[_]: Monad, Req] extends Service[F, Req, Req] {
    override def run(req: Req): F[Req] = Monad[F].pure(req)
  }

  behavior of "An Arrow Service"
  it should "allow me to use andThen" in {
    val msg = "Hello World"
    val expected = msg.toUpperCase.concat("!")
    val echo = new EchoService()
    val toUpper = new ToUpperService()
    val addEmphasis = new AddEmphasisService()
    val logger: RequestLogger[Future, String] = new RequestLogger[Future, String]

    val pipeline = logger andThen toUpper andThen echo andThen addEmphasis andThen logger
    val result = Await.result(pipeline(msg), Duration.Inf)
    val echoResult = Await.result(echo(msg), Duration.Inf)
    echoResult should be(msg)

    result should be(expected)
  }
}
