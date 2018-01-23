package com.gilesc
package arrow

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import cats.Monad
import cats.data.Kleisli
import cats.implicits._

class FilterSpec extends FlatSpec with Matchers {

  final class EchoService extends Service[Future, String, String] {
    override def run(req: String): Future[String] = Future.successful(req)
  }

  final class ToUpperFilter extends SimpleFilter[Future, String, String] {
    override def run(
      x: String,
      service: Service[Future, String, String]
    ): Future[String] = service(x.toUpperCase())
  }

  final class AddEmphasisFilter extends SimpleFilter[Future, String, String] {
    override def run(
      str: String,
      service: Service[Future, String, String]
    ): Future[String] = service(str.concat("!"))
  }

  final class RequestLogger[F[_]: Monad, Req] extends SimpleFilter[F, Req, Req] {
    override def run(
      req: Req,
      service: Service[F, Req, Req]
    ): F[Req] = service(req)
  }

  final class IntToStringFilter extends Filter[Future, Int, String, String, String] {
    override def run(
      value: Int,
      service: Service[Future, String, String]
    ): Future[String] = service(value.toString)
  }

  behavior of "An Arrow Filter"
  it should "allow me to use andThen" in {
    val msg = "Hello World"
    val expected = msg.toUpperCase.concat("!")
    val echo = new EchoService()
    val toUpper = new ToUpperFilter()
    val addEmphasis = new AddEmphasisFilter()
    val logger = new RequestLogger[Future, String]
    val result = toUpper andThen logger andThen addEmphasis andThen echo

    val fin = Await.result(result(msg), Duration.Inf)
    fin should be(expected)
  }


  it should "let me transform stuff" in {
    val intToString = new IntToStringFilter()
    val addEmphasis = new AddEmphasisFilter()
    val echo = new EchoService()
    val expected = "42!"
    val number = 42

    val pipeline: Kleisli[Future, Int, String] = intToString andThen addEmphasis andThen echo
    val msg = Await.result(pipeline(number), Duration.Inf)
    msg should be(expected)
  }
}


