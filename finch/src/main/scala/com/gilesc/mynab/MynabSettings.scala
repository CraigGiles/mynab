package com.gilesc
package mynab

object MynabSettings {
  import com.typesafe.config.ConfigFactory

  private[this] val config = ConfigFactory.load()
  val actorSystemName = config.getString("mynab.akka-http.actor-system-name")
  val sessionKey = "really-long-string-that-needs-to-be-generated"

  val clientId = config.getString("mynab.oauth2.client-id")
}
