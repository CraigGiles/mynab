package com.gilesc
package mynab
package service

import org.scalacheck.Gen
import doobie.scalatest._
import doobie.util.transactor.Transactor

import cats.effect.IO

import com.gilesc.mynab.testkit.TestCase

import com.gilesc.mynab.repository.mysql._
import com.gilesc.mynab.testkit.DatabaseTestCase
import com.gilesc.mynab.testkit.TestCase
import com.gilesc.mynab.repository._
import cats.effect.IO
import cats.data.EitherT
import cats.syntax.either._

class MysqlCategoryRepositorySpec extends DatabaseTestCase {
  val userId = UserId(1)
    case class DatabaseConfig(
      driver: String,
      url: String,
      username: String,
      password: String
    )
    val config = pureconfig.loadConfigOrThrow[DatabaseConfig]("mynab.database")

  def transactor = Transactor.fromDriverManager[IO](
    config.driver,
    config.url,
    config.username,
    config.password)

  private val groupRepo = new MysqlCategoryGroupRepository[IO](transactor)
  private val categoryRepo = new MysqlCategoryRepository[IO](transactor)

  behavior of "Category Service With MySQL"
  it should "allow me to store a category with new major / minor names" in {
    val service = CreateCategoryService.apply[IO](groupRepo, categoryRepo)
    val majorName = CategoryName(Gen.alphaStr.sample.get)
    val minorName = CategoryName(Gen.alphaStr.sample.get)
    val ctx = CreateCategoryContext(userId, majorName, minorName)
    val Right(result) = service(ctx).unsafeRunSync
    result.group.name should be(majorName)
    result.name should be(minorName)
  }

  it should "allow me to store a category with existing major name, new minor name" in {
    val service = CreateCategoryService.apply[IO](groupRepo, categoryRepo)
    val majorName = CategoryName(Gen.alphaStr.sample.get)
    val firstMinor = CategoryName(Gen.alphaStr.sample.get)
    val secondMinor = CategoryName(Gen.alphaStr.sample.get)

    val firstCtx = CreateCategoryContext(userId, majorName, firstMinor)
    val secondCtx = CreateCategoryContext(userId, majorName, secondMinor)
    val Right(firstResult) = service(firstCtx).unsafeRunSync
    firstResult.group.name should be(majorName)
    firstResult.name should be(firstMinor)

     val Right(secondResult) = service(secondCtx).unsafeRunSync
     secondResult.group.name should be(majorName)
     secondResult.name should be(secondMinor)
  }

  it should "give me a proper error when no user is found" in {
    val service = CreateCategoryService.apply[IO](groupRepo, categoryRepo)
    val majorName = CategoryName(Gen.alphaStr.sample.get)
    val minorName = CategoryName(Gen.alphaStr.sample.get)
    val ctx = CreateCategoryContext(UserId(Long.MaxValue), majorName, minorName)
    val Left(result) = service(ctx).unsafeRunSync
    result should be("ForeignKeyConstraint")
  }

}



