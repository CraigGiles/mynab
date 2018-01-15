package com.gilesc
package mynab
package repository

import com.gilesc.mynab.testkit.TestCase
import org.scalatest.{Assertions, FunSuite}
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

class CategoryRepositorySpec extends InMemoryDatabase with IOChecker {
  import DatabaseConfig._

  type UserDTO = (Int, String, String)
  val config = DatabaseConfig()

  override def transactor = Transactor.fromDriverManager[IO](
    config.driver,
    config.url,
    config.username,
    config.password)

  private val repo = new CategoryGroupRepositoryMysql[IO](transactor)

  behavior of "Category Group Repository"
  it should "allow me to store a category group" in {
    val name = CategoryName("hello world")
    val ctx = new CategoryGroupContext(name)
    val Right(result) = repo.create(ctx).unsafeRunSync
    result should be(CategoryGroup(CategoryGroupId(1), name))
  }

  it should "give me a duplicate key error when inserting the same group name" in {
    val name = CategoryName("mycategoryname")
    val ctx = new CategoryGroupContext(name)
    val Right(first) = repo.create(ctx).unsafeRunSync
    val Left(second) = repo.create(ctx).unsafeRunSync

    first.name should be(name)
    second should be(PersistenceError.DuplicateKey)
  }

  it should "allow me to test something" in {
    val name = CategoryName("wow what a difference a day makes")
    val ctx = new CategoryGroupContext(name)
    val result01 = repo.create(ctx).unsafeRunSync
    val result02 = repo.create(ctx).unsafeRunSync
  }

}

