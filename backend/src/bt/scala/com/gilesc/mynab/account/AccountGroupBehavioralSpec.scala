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
import scala.language.reflectiveCalls

abstract class BehavioralTestCase extends TestCase with BeforeAndAfterAll {
  val fixture = new {
    val database = SlickDatabaseProfile(ConfigFactory.load())
    val AccountGroupTable = {
      import database.profile.api._
      TableQuery[AccountGroupDataModule.AccountGroupTable]
    }
  }

}

class AccountGroupBehavioralSpec extends BehavioralTestCase {
  override def beforeAll(): Unit = {
    super.beforeAll()

    import fixture.database.profile.api._

    val invalidGroupInsert = sqlu"insert into account_groups (user_id, name) values (1, 'x')"
    val selectNameFromGroup = sql"select name from account_groups".as[String]

    def waitfor[A](fut: scala.concurrent.Future[A]): A =
      scala.concurrent.Await.result(fut, scala.concurrent.duration.Duration.Inf)

    val waiton = for {
      _ <- fixture.database.execute(fixture.AccountGroupTable.schema.create)
      _ <- fixture.database.execute(invalidGroupInsert)
      c <- fixture.database.execute(selectNameFromGroup)
    } yield c

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
