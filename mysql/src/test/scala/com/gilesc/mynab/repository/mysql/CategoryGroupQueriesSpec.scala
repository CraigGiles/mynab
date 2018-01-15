package com.gilesc
package mynab
package repository
package mysql

import doobie.scalatest._
import doobie.util.transactor.Transactor

import cats.effect.IO

import com.gilesc.mynab.testkit.TestCase

class CategoryGroupQueriesSpec extends TestCase
  with IOChecker
  with CategoryGroupQueries {

  val config = DatabaseConfig()
  override def transactor = Transactor.fromDriverManager[IO](
    config.driver,
    config.url,
    config.username,
    config.password)

  behavior of "Category Group Repository Queries"
  it should "have valid queries" in {
    val name = CategoryName("hello world")
    check(insertQuery(name))
  }
}
