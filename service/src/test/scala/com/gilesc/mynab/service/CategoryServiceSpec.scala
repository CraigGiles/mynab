package com.gilesc
package mynab
package service

import com.gilesc.mynab.testkit.TestCase
import com.gilesc.mynab.repository._
import cats.effect.IO
import cats.data.EitherT
import cats.syntax.either._

class CategoryServiceSpec extends TestCase {
  val groups = new CategoryGroupRepository[IO] {
    override def create(
      ctx: CategoryGroupContext
    ): IO[Either[RepositoryError, CategoryGroup]] = {
      IO.pure(Left(RepositoryError.DuplicateKey))
    }
    override def find(name: CategoryName): IO[Option[CategoryGroup]] =
      IO.pure(None)
  }

  val categories = new CategoryRepository[IO] {
    override def create(
      ctx: CategoryContext
    ): IO[Either[RepositoryError, Category]] = {
      IO.pure(Left(RepositoryError.DuplicateKey))
    }
  }


  behavior of "Category Service"
  it should "allow me to create a new category" in {
    // TODO fix thix unit test
    val service = CreateCategoryService.apply[IO](groups, categories)
    val food = CategoryName("Food")
    val diningOut = CategoryName("Dining Out")
    val ctx = CreateCategoryContext(food, diningOut)
    val Left(result) = service(ctx).unsafeRunSync
    result shouldBe a [String]
  }

}
