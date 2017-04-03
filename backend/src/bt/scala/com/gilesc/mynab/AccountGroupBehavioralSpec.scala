package com.gilesc
package mynab

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
}
