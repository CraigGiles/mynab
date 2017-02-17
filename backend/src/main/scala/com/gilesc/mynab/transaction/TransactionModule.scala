package com.gilesc.mynab.transaction

import com.gilesc.mynab.category.Category

trait TransactionModule {
  type TransactionState = Vector[Transaction]

  def toggleCleared: (Vector[Transaction], TransactionState) => TransactionState
  def sum: Vector[Transaction] => BigDecimal
  def recategorize: (Category, Vector[Transaction]) => Vector[Transaction]
}
