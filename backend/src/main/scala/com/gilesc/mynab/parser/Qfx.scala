package com.gilesc.mynab
package parser

import java.io.File

import com.gilesc.mynab.transaction._

object Qfx {
  def parse(file: File): List[Transaction] = {
    List.empty[Transaction]
  }

  def fileToStrList(file: File): List[String] = {
    if (file.canRead) println("CAN READ FILE") else sys.error("CANNOT READ FILE")
    List.empty[String]
  }

}
