package com.gilesc.mynab.transaction

import java.time.LocalDate

import com.gilesc.mynab.category._
import com.gilesc.mynab.logging.LoggingModule

import scala.util.Try

case class TransactionDetails(
  date: String, payee: String, majorCat: String, minorCat: String, memo: String,
  deposit: Double, withdrawal: Double, cleared: Boolean)

sealed trait CreateResult
final case class Success(transaction: Transaction) extends CreateResult
final case class Failure(message: String) extends CreateResult

trait TransactionServiceModule {
  def create: TransactionDetails => CreateResult
}

object TransactionService {
  def create(accounts: AccountRepositoryModule,
    log: LoggingModule)(details: TransactionDetails): CreateResult = {

    log.info(s"Adding Transaction: (${details.date}, ${details.payee})")

    val results = convert(details)
    // TODO: Event-source the transaction event
    // TODO: prepend the transaction to accounts transaction list

    results match {
      case Right(t) => Success(t)
      case Left(s) => Failure(s)
    }
  }

  def parseDate(value: String): Either[String, LocalDate] =
    Try(LocalDate.parse(value)).toEither.left.map(_.getMessage)

  val convert: TransactionDetails => Either[String, Transaction] = { details =>
    parseDate(details.date) map { date =>
      val payee = Payee("Uncle Bob")
      val majorCat = MajorCategory("Education")
      val minorCat = MinorCategory("Books")
      val memo = Memo("New programming book from Uncle Bob")
      val deposit = Amount(BigDecimal(0.0))
      val withdrawal = Amount(BigDecimal(39.99))
      val cleared = Cleared(false)

      Transaction.apply(date, payee, Category(majorCat, minorCat), memo, withdrawal, deposit, cleared)
    }
  }

}

// --------------------------------------------------------------------------
// Move to its own file
// --------------------------------------------------------------------------
sealed trait PersistenceResult
case object PersistenceSuccessful extends PersistenceResult
case class PersistenceFailure(message: String) extends PersistenceResult

sealed trait FindBy
final case class FindByPayee(payee: Payee) extends FindBy

trait AccountRepositoryModule {
  def save: Transaction => PersistenceResult
  def find: FindBy => Option[Transaction]
}

object InMemoryTransactionRepository extends AccountRepositoryModule {
  var transaction = Vector.empty[Transaction]

  def save: Transaction => PersistenceResult = { account =>
    println(s"Persisting $account")
    transaction = transaction :+ account
    PersistenceSuccessful
  }

  def find: FindBy => Option[Transaction] = {
    case FindByPayee(payee) => transaction.find(_.payee == payee)
  }
}

