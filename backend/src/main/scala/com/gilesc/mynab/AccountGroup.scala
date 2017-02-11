package com.gilesc.mynab
package account

import com.gilesc.commons.{Prepending, Removing}

trait AccountGroupModule { self: Prepending =>
  type AccountState = List[Account]
}

object AccountGroup extends AccountGroupModule with Prepending {
}

// Account List Domain Objects
// ------------------------------------------------------------------------

