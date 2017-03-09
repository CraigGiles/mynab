package com.gilesc.mynab
package account

import cats.data.State
import com.gilesc.commons.{Prepending, Removing}

trait AccountGroupModule { self: Prepending with Removing =>
  val create: (AccountGroupId, AccountName) => AccountGroup
  val add: Account => State[AccountGroup, Unit]
  val sum: Vector[Account] => BigDecimal
}
