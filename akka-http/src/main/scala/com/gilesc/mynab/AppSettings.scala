package com.gilesc
package mynab

object AppSettings {
  import com.typesafe.config.ConfigFactory

  private[this] val config = ConfigFactory.load()
  val actorSystemName = config.getString("mynab.akka-http.actor-system-name")
  val host = "localhost"
  val port = 8080

  // val realm = "realm"
}
