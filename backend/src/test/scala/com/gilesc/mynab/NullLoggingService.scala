package com.gilesc
package mynab

import com.gilesc.mynab.logging.LoggingModule

object NullLoggingService extends LoggingModule {
  val debug: String => Unit = str => ()
  val info: String => Unit = str => ()
  val warn: String => Unit = str => ()
  val error: String => Unit = str => ()
}
