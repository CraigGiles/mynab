package com.gilesc
package mynab
package account

import java.time.LocalDate

import com.gilesc.mynab.account._
import com.gilesc.mynab.user._
import com.gilesc.mynab.category._
import com.gilesc.mynab.transaction._
import com.gilesc.mynab.persistence.account._
import com.gilesc.mynab.persistence.mysql.SlickDatabaseProfile
import com.typesafe.config.ConfigFactory
import org.scalatest.BeforeAndAfterAll

import scala.concurrent.ExecutionContext.Implicits.global

abstract class BehavioralTestCase extends TestCase with BeforeAndAfterAll {

}

class AccountGroupBehavioralSpec extends BehavioralTestCase {
  def fixture = new {
    // TODO: Yeah... lets get rid of this somehow
    def waitfor[A](fut: scala.concurrent.Future[A]): A =
      scala.concurrent.Await.result(fut, scala.concurrent.duration.Duration.Inf)

    val db = SlickDatabaseProfile(ConfigFactory.load())
    import db.profile.api._
    val create = db.execute(AccountGroupRepository.AccountGroupTable.schema.create)
    waitfor(create)
  }

  override def beforeAll(): Unit = {
    super.beforeAll()

    // TODO: Yeah... lets get rid of this somehow
    def waitfor[A](fut: scala.concurrent.Future[A]): A =
      scala.concurrent.Await.result(fut, scala.concurrent.duration.Duration.Inf)

    val db = SlickDatabaseProfile(ConfigFactory.load())
    import db.profile.api._
    val waiton = db.execute(AccountGroupRepository.AccountGroupTable.schema.create) map { a =>
      val statement = sqlu"insert into account_groups (user_id, name) values (1, 'x')"
      waitfor(db.execute(statement))
      val res = db.execute(sql"select name from account_groups".as[String])
      val result = waitfor(res)
      println("RESULTS! -----------------------------------" + result)
      // scala.concurrent.Future(result)
      result
    }

    waitfor(waiton)
    ()
  }

  import AccountGroupService._
  val userId = UserId(1L)

  behavior of "AccountGroups"
  it should "persist the group in a database" in {
    val before = AccountGroupRepository.count()

    val id = before + 1L
    val name = "newaccountgroup"
    val expected = AccountGroup.create(id, name)

    AccountGroupService.createWithPersistence(userId, name) should be(Right(expected))
    AccountGroupRepository.count() should be(id)
  }

   it should "be able to find by name" in {
     val name = "Budget Accounts"
     val expected = AccountGroupService.createWithPersistence(userId, name).right.get

     AccountGroupService.findWithPersistence(FindByName(name)) should
       be(Right(Some(expected)))
   }

  it should "be able to guard against bad name in the database" in {
    val name = "x"


    AccountGroupService.findWithPersistence(FindByName(name)) should
      be(Left("x is not a valid account name"))
  }
}
