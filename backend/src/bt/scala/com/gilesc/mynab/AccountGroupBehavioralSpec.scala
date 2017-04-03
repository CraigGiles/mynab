package com.gilesc
package mynab

import java.time.LocalDate

import com.gilesc.mynab.account._
import com.gilesc.mynab.category._
import com.gilesc.mynab.transaction._

import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback

import com.gilesc.mynab.persistence.account._
import com.gilesc.mynab.persistence.DatabaseInitializer

import org.scalatest.fixture.FlatSpec
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll

abstract class BehavioralTestCase extends FlatSpec 
  with Matchers
  with TestCaseHelpers
  with AutoRollback
  with BeforeAndAfterAll {

  override def beforeAll() = {
    DatabaseInitializer.init()
    MockDatabase.migrate()
  }

  override def afterAll() = {
    DatabaseInitializer.stop()
  }

}

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
}
