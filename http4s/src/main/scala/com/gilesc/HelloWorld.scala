package com.gilesc

import argonaut.Argonaut._
import org.http4s._
import org.http4s.dsl._
import org.http4s.argonaut._

object HelloWorld {
  val service = HttpService {
    case GET -> Root / "hello" / name =>
      Ok(jSingleObject("message", jString(s"Hello, ${name}")))
  }
}
