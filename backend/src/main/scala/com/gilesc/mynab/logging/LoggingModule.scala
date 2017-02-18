package com.gilesc.mynab
package logging

trait LoggingModule {
  def debug: String => Unit
  def info: String => Unit
  def warn: String => Unit
  def error: String => Unit
}

object PrintlnLoggingService extends LoggingModule {
  val debug: String => Unit = println
  val info: String => Unit = println
  val warn: String => Unit = println
  val error: String => Unit = println
}
