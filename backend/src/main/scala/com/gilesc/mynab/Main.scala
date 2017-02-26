package com.gilesc.mynab

import com.gilesc.mynab.account._
import com.gilesc.mynab.logging.PrintlnLoggingService
import com.gilesc.mynab.repository.InMemoryAccountRepository

object Main extends App with AccountServiceModule {
  val details = AccountDetails(1L, "My Account Name", "Banking", "No Group Name")

  def create = AccountService.create(InMemoryAccountRepository.save, PrintlnLoggingService)
  def find = AccountService.find(InMemoryAccountRepository.find, PrintlnLoggingService)
  val account = create(details)

  println(account)
}
