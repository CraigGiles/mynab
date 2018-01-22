package com.gilesc
package arrow

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import cats.effect.IO
import cats.Monad

class ServiceSpec extends FlatSpec with Matchers {

  final class EchoService extends Service[IO, String, String] {
    override def apply(value: String): IO[String] = IO.pure(value)
  }

  final class ToUpperService extends Service[IO, String, String] {
    override def apply(msg: String): IO[String] = IO.pure(msg.toUpperCase)
  }

  final class AddEmphasisService extends Service[IO, String, String] {
    override def apply(msg: String): IO[String] = IO.pure(msg + "!")
  }

  final class RequestLogger[F[_]: Monad, Req] extends Service[F, Req, Req] {
    override def apply(request: Req): F[Req] = {
      Monad[F].pure(request)
    }
  }

  behavior of "An Arrow Service"
  it should "allow me to use andThen" in {
    val msg = "Hello World"
    val expected = msg.toUpperCase.concat("!")
    val echo = new EchoService()
    val toUpper = new ToUpperService()
    val addEmphasis = new AddEmphasisService()
    val logger: RequestLogger[IO, String] = new RequestLogger[IO, String]

    val pipeline = logger andThen toUpper andThen echo andThen addEmphasis andThen logger
    val result = pipeline(msg).unsafeRunSync()
    result should be(expected)
  }

  it should "allow me to use pipeTo" in {
    val msg = "Hello World"
    val expected = msg.toUpperCase.concat("!")
    val echo = new EchoService()
    val toUpper = new ToUpperService()
    val addEmphasis = new AddEmphasisService()
    val logger: RequestLogger[IO, String] = new RequestLogger[IO, String]

    val pipeline = logger pipeTo toUpper pipeTo echo pipeTo addEmphasis pipeTo logger
    val result = pipeline(msg).unsafeRunSync()
    result should be(expected)
  }
}
