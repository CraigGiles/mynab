package com.gilesc
package mynab
package persistence

import com.typesafe.config.Config

trait DatabaseEnv {
  val config: Config
}

trait DatabaseProfile
