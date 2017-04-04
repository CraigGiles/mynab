package com.gilesc
package mynab
package account

import java.time.LocalDate

import com.gilesc.mynab.account._
import com.gilesc.mynab.category._
import com.gilesc.mynab.transaction._
import com.gilesc.mynab.persistence.account._

import scalikejdbc._

class AccountGroupBehavioralSpec extends BehavioralTestCase {
  override def fixture(implicit session: DBSession) = {
    ()
  }

  behavior of "AccountGroups"
  it should "persist the group in a database" in { implicit session =>
    val before = AccountGroupRepository.count()
    val id = before + 1L
    val name = "newaccountgroup"
    val expected = AccountGroup.create(id, name)

    AccountGroupService.createWithPersistence(name) should be(Right(expected))
    AccountGroupRepository.count() should be(id)
  }

  it should "be able to find by name" in { implicit session =>
    import AccountGroupService._

    val name = "Budget Accounts"
    val expected = AccountGroupService.createWithPersistence(name).right.get

    AccountGroupService.findWithPersistence(FindByName(name)) should
      be(Right(Some(expected)))
  }

  it should "be able to guard against bad name in the database" in {
    implicit session =>
    import AccountGroupService._
    val name = "x"

    sql"""
      |INSERT INTO account_groups (name)
      |VALUES
      |  ('x');
      """.stripMargin('|').updateAndReturnGeneratedKey.apply()

    AccountGroupService.findWithPersistence(FindByName(name)) should
      be(Left("x is not a valid account name"))
  }
}
