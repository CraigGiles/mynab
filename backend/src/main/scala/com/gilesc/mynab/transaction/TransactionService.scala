package com.gilesc.mynab.transaction

import java.time.LocalDate

import com.gilesc.mynab.account.{Account, AccountId}
import com.gilesc.mynab.category._
import com.gilesc.mynab.logging.LoggingModule

import scala.util.Try

case class TransactionDetails(
  accountId: Long, transactionId: Long,
  date: String, payee: String, majorCat: String, minorCat: String, memo: String,
  deposit: Double, withdrawal: Double, cleared: Boolean)

sealed trait CreateResult
final case class Success(transaction: Transaction) extends CreateResult
final case class Failure(message: String) extends CreateResult

trait TransactionServiceModule {
  def create: TransactionDetails => CreateResult
}

object TransactionService {
  import com.gilesc.mynab.repository.{FindBy => AccountFindBy,
    FindById => AccountFindById }

  def create(
    findAccount: AccountFindBy => Option[Account],
    saveTransaction: Transaction => Either[PersistenceFailure, Transaction],
    log: LoggingModule)(details: TransactionDetails): CreateResult = {

    log.info(s"Adding Transaction: (${details.date}, ${details.payee})")

    val persistedTransaction = convert(details) flatMap { transaction =>
      // TODO: Event-source the transaction event
      saveTransaction(transaction)
//      val a = findAccount(AccountFindById(AccountId(details.accountId)))
//      val asdf = Account.add(transaction).runS(a.get).value
//      println(asdf)
    }

    val results = persistedTransaction.left.map(_.toString)

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
      val transactionId = TransactionId(details.transactionId)
      val payee = Payee("Uncle Bob")
      val majorCat = MajorCategory("Education")
      val minorCat = MinorCategory("Books")
      val memo = Memo("New programming book from Uncle Bob")
      val deposit = Amount(BigDecimal(0.0))
      val withdrawal = Amount(BigDecimal(39.99))
      val cleared = Cleared(false)

      Transaction.apply(transactionId, date, payee, Category(majorCat, minorCat),
        memo, withdrawal, deposit, cleared)
    }
  }

}


// --------------------------------------------------------------------------
// Move to its own file
// --------------------------------------------------------------------------

sealed trait PersistenceResult
case class PersistenceSuccessful[T](value: T) extends PersistenceResult
case class PersistenceFailure(message: String) extends PersistenceResult

sealed trait FindBy
final case class FindByPayee(payee: Payee) extends FindBy

trait AccountRepositoryModule {
  def save: Transaction => Either[PersistenceFailure, Transaction]
  def find: FindBy => Option[Transaction]
}

object InMemoryTransactionRepository extends AccountRepositoryModule {
  private[this] var transactions = Vector.empty[Transaction]

  def save: Transaction => Either[PersistenceFailure, Transaction] = { t =>
    println(s"Persisting $t")
    transactions = transactions :+ t
    Right(t)
  }

  def find: FindBy => Option[Transaction] = {
    case FindByPayee(payee) => transactions.find(_.payee == payee)
  }
}

