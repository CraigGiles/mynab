package com.gilesc
package mynab
package persistence

import com.gilesc.mynab.persistence.mysql._

object Main extends App {
  import java.time._
  import com.gilesc.mynab.transaction._
  import com.gilesc.mynab.account._
  import com.gilesc.mynab.category._

  val aid = AccountId(1L)
  val date = LocalDate.now()
  val payee = Payee("GNG Pest Inspection")
  val category = Category(MajorCategory("Inspections"), MinorCategory("Pest/Termite"))
  val memo = Memo("Pest Inspection for Sale of home")
  val withdrawal = Amount(BigDecimal(200))
  val deposit = Amount(BigDecimal(0))
  val cleared = Cleared(false)
  val t = TransactionContext(aid, date, payee, category, memo, deposit, withdrawal, cleared)

  println("CREATED! : " + TransactionRepository.create(t))
  TransactionRepository.all map println
}

