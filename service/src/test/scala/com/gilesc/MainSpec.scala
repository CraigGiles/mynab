package com.gilesc

import org.scalatest.FlatSpec
import org.scalatest.Matchers

class MainSpec extends FlatSpec with Matchers {
  "the hello object" should "have a hello message" in {
    Hello.hello should be("Hello World!")
  }
}
