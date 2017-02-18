package com.gilesc.mynab
package logging

trait LoggingModule {
  def log: String => Unit
}

object PrintlnLoggingService extends LoggingModule {
  def log: String => Unit = println
}
