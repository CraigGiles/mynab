package com.gilesc
package mynab
package repository

import com.gilesc.mynab.testkit.TestCase
import org.scalatest.{Assertions, FunSuite}
import doobie._
import doobie.implicits._
import doobie.scalatest._
import doobie.util.transactor.Transactor
import doobie.util.transactor.Strategy
import doobie.h2._
import doobie.h2.implicits._
import cats._
import cats.data._
import cats.effect.{Async, IO}
import cats.implicits._

class UserRepositoryQuerySpec extends InMemoryDatabase with IOChecker {
  import DatabaseConfig._

  type UserDTO = (Int, String, String)
  val config = DatabaseConfig()
  override def transactor = Transactor.fromDriverManager[IO](
    config.driver,
    config.url,
    config.username,
    config.password)

  private val userRepo = new MysqlUserRepository[IO](transactor)

  behavior of "User Repository"
  it should "let me get a user" in {
    def insert(name: String, email: String): Update0 =
      sql"insert into api_user (username, email) values ($name, $email)".update

//    val userMock = (1, "gvolpe", "forget")
//    val cs: ConnectionIO[(Int, String, String)] = FC.delay(userMock)
//    val result = cs.transact(transactor).unsafeRunSync()

    val username = new UserName("gvolpe")
    insert(username.value, "email").run.transact(transactor).unsafeRunSync
    val user = userRepo.userQuery(username)
    println("PRINTING USER: " + user.list.transact(transactor).unsafeRunSync())
    val result = user.list.transact(transactor)
    println(result)
  }
}

