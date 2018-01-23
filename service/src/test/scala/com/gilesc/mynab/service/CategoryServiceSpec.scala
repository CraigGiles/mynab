package com.gilesc
package mynab
package service

import com.gilesc.mynab.testkit.TestCase
import com.gilesc.mynab.repository._
import cats.effect.IO
import cats.data.{EitherT, Kleisli}
import cats.syntax.either._
import com.gilesc.arrow._

class CategoryServiceSpec extends TestCase {
  // val groups = new CategoryGroupRepository[IO] {
  //   override def create(
  //     ctx: CategoryGroupContext
  //   ): IO[Either[RepositoryError, CategoryGroup]] = {
  //     IO.pure(Left(RepositoryError.DuplicateKey))
  //   }
  //   override def find(name: CategoryName): IO[Option[CategoryGroup]] =
  //     IO.pure(None)
  // }

  // val categories = new CategoryRepository[IO] {
  //   override def create(
  //     ctx: CategoryContext
  //   ): IO[Either[RepositoryError, Category]] = {
  //     IO.pure(Left(RepositoryError.DuplicateKey))
  //   }
  // }


  import com.gilesc.mynab.{Category => MynabCategory}
  val filter = new Filter[IO, (String, String), MynabCategory, CreateCategoryContext, MynabCategory] {
    /**
      * The logic needed to run your filter goes here
      */
    override def run(
      request: (String, String),
      service: Service[IO, CreateCategoryContext, MynabCategory]
    ): IO[MynabCategory] = {
      val (major, minor) = request
      val ctx = CreateCategoryContext(
        CategoryName(major),
        CategoryName(minor))

      service(ctx)
    }
  }
  val service = new Service[IO, CreateCategoryContext, MynabCategory] {
    override def run(req: CreateCategoryContext): IO[MynabCategory] = {
      IO.pure(MynabCategory(MajorCategory(req.major.value), MinorCategory(req.minor.value)))
    }
  }

   behavior of "Category Service"
   it should "allow me to create a new category" in {
     val food = CategoryName("Food")
     val diningOut = CategoryName("Dining Out")
     val ctx = CreateCategoryContext(food, diningOut)
     val result = service(ctx).unsafeRunSync()
     result.major.value should be(food.value)
     result.minor.value should be(diningOut.value)

     val pipeline: Service[IO, (String, String), MynabCategory] = filter andThen service
     val pipelineResult = pipeline((food.value, diningOut.value)).unsafeRunSync()
     pipelineResult.major.value should be(food.value)
     pipelineResult.minor.value should be(diningOut.value)
   }

}
