package com.gilesc

import argonaut.Argonaut._
import org.http4s.HttpService
import org.http4s.dsl.{->, /, Root, _}

/**
  * Created by craiggiles on 2/17/17.
  */
object HelloWorld {
  val service = HttpService {
    case GET -> Root / "hello" / name =>
      Ok(jSingleObject("message", jString(s"Hello, ${name}")))
  }
}
