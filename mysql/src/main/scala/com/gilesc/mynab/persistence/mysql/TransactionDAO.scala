package com.gilesc
package mynab
package persistence
package mysql

import scalikejdbc._
import com.gilesc.mynab.transaction._
import com.gilesc.mynab.category._

object TransactionDAO {
  def fromDb(rs: WrappedResultSet): Transaction = {
    val id = TransactionId(rs.long("id"))
    val date = rs.date("date").toLocalDate
    val payee = Payee(rs.string("payee"))
    val minorCat = MinorCategory(rs.string("minor_category"))
    val majorCat = MajorCategory(rs.string("major_category"))
    val memo = Memo(rs.string("memo"))
    val withdrawal = Amount(rs.bigDecimal("withdrawal"))
    val deposit = Amount(rs.bigDecimal("deposit"))
    val cleared = Cleared(rs.boolean("cleared"))

    Transaction(id, date, payee, Category(majorCat, minorCat), memo, withdrawal, deposit, cleared)
  }

}

object TransactionRepository {
  Initializer.init()
  implicit val session = AutoSession

  val createString = sql"""
     |CREATE TABLE `transactions` (
     |  `id` BIGINT(20) unsigned NOT NULL AUTO_INCREMENT,
     |  `date` DATE NOT NULL,
     |  `payee` VARCHAR(120) NOT NULL,
     |  `minor_category` VARCHAR(120) NOT NULL,
     |  `major_category` VARCHAR(120) NOT NULL,
     |  `memo` VARCHAR(255) NOT NULL,
     |  `withdrawal` DECIMAL NOT NULL,
     |  `deposit` DECIMAL NOT NULL,
     |  `cleared` BIT NOT NULL,
     |  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
     |  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
     |  `deleted_at` timestamp NULL DEFAULT NULL,
     |  PRIMARY KEY (`id`)
     |) ENGINE=InnoDB;
        """.stripMargin('|').execute().apply()

  def all: Seq[Transaction] = DB.readOnly { implicit session =>
    sql"select * from transactions".map(TransactionDAO.fromDb).list().apply()
  }

  def create(t: TransactionContext) = DB autoCommit { implicit session =>
    val id = sql"""
    |INSERT INTO transactions (date, payee, major_category, minor_category, memo, withdrawal, deposit, cleared)
    |VALUES
    |  (${t.date}, ${t.payee.value}, ${t.category.major.value}, ${t.category.minor.value}, ${t.memo.value}, ${t.withdrawal.value}, ${t.deposit.value}, ${t.cleared.value});
    """.stripMargin('|').updateAndReturnGeneratedKey.apply()

    Transaction.create(TransactionId(id), t)
}
}
