package com.gilesc.mynab
package parser

import com.gilesc.mynab.transaction._

import scala.io.Source

class QfxSpec extends TestCase {
  import Qfx._

  "QFX Parser" should {
    "be able to take a file and return a list of transactions" in {
      val readmeText = Source.fromResource("example.QFX").getLines()
//      val file = new File("/example.qfx")
//      val list = parse(file)
      println(readmeText.toList)
    }
  }
}
