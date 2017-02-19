package com.gilesc.mynab
package repository

import com.gilesc.mynab.account.{Account, AccountName}

sealed trait PersistenceResult
case object PersistenceSuccessful extends PersistenceResult
case class PersistenceFailure(message: String) extends PersistenceResult

sealed trait FindBy
final case class FindByName(name: AccountName) extends FindBy

trait AccountRepositoryModule {
  def save: Account => PersistenceResult
  def find: FindBy => Option[Account]
}

object InMemoryAccountRepository extends AccountRepositoryModule {
  var accounts = Vector.empty[Account]

  def save: Account => PersistenceResult = { account =>
    println(s"Persisting $account")
    accounts = accounts :+ account
    PersistenceSuccessful
  }

  def find: FindBy => Option[Account] = {
    case FindByName(name) => accounts.find(_.name == name)
  }
}


