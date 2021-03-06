package com.gilesc
package mynab
package repository
package mysql

import doobie.scalatest._
import doobie.util.transactor.Transactor

import cats.effect.IO

import com.gilesc.mynab.testkit.TestCase
import com.gilesc.mynab.testkit.DatabaseTestCase

import com.spotify.docker.client.DefaultDockerClient
import com.whisk.docker.{DockerFactory, DockerKit}
import com.whisk.docker.impl.spotify._

class MysqlCategoryRepositorySpec extends TestCase with DockerMysqlService {
  val config = DatabaseConfig()

  override implicit val dockerFactory: DockerFactory = new SpotifyDockerFactory(
    DefaultDockerClient.fromEnv().build())

  def transactor = Transactor.fromDriverManager[IO](
    config.driver,
    config.url,
    config.username,
    config.password)

  private val repo = new MysqlCategoryGroupRepository[IO](transactor)

  behavior of "Category Group Repository"
  it should "allow me to store a category group" in {
    val userId = UserId(1L)
    val name = CategoryName("hello world")
    val ctx = new CategoryGroupContext(userId, name)
    val Right(result) = repo.create(ctx).unsafeRunSync
    result should be(CategoryGroup(CategoryGroupId(1), userId, name))
  }

  it should "give me a duplicate key error when inserting the same group name" in {
    val userId = UserId(1L)
    val name = CategoryName("mycategoryname")
    val ctx = new CategoryGroupContext(userId, name)
    val Right(first) = repo.create(ctx).unsafeRunSync
    val Left(second) = repo.create(ctx).unsafeRunSync

    first.name should be(name)
    second should be(RepositoryError.DuplicateKey)
  }

  behavior of "Category Repository"
  it should "allows me to store a category" in {
    val userId = UserId(1L)
    val catRepo = new MysqlCategoryRepository[IO](transactor)
    val name = CategoryName("Food")
    val ctx = new CategoryGroupContext(userId, name)
    val Right(group) = repo.create(ctx).unsafeRunSync

    val categoryName = CategoryName("Groceries")
    val catContext = new CategoryContext(userId, group, categoryName)
    val category = catRepo.create(catContext).unsafeRunSync
  }

}

