package com.gilesc.mynab
package account

import cats.data.State
import com.gilesc.commons.{Prepending, Removing}

trait AccountGroupModule { self: Prepending with Removing =>
  val newAccountGroup: (String) => AccountGroup
  val addAccount: Account => State[AccountGroup, Unit]
  val sumAllAccounts: Vector[Account] => BigDecimal
}
