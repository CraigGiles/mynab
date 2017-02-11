package com.gilesc.mynab
package account

trait AccountGroupModule {
  type AccountState = List[Account]

  def appendAccount: (Account, AccountState) => AccountState
}

object AccountGroup extends AccountGroupModule {
  val appendAccount: (Account, AccountState) => AccountState = (a, al) => a :: al
}

// Account List Domain Objects
// ------------------------------------------------------------------------

