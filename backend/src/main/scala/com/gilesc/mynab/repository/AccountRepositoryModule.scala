package com.gilesc.mynab
package repository

import com.gilesc.mynab.account.Account

sealed trait PersistenceResult
case object PersistenceSuccessful extends PersistenceResult
case class PersistenceFailure(message: String) extends PersistenceResult

trait AccountRepositoryModule {
  def save: Account => PersistenceResult
}

object InMemoryAccountRepository extends AccountRepositoryModule {
  def save: Account => PersistenceResult = { account =>
    println(s"Persisting $account")
    PersistenceSuccessful
  }
}


