package com.gilesc
package mynab
package service

import com.gilesc.mynab.testkit.TestCase
import com.gilesc.mynab.repository._
import cats.effect.IO
import com.gilesc.arrow._

class CategoryServiceSpec extends TestCase {
  val filter = new Filter[IO, (String, String), Category, CreateCategoryContext, Category] {
    override def run(
      request: (String, String),
      service: Service[IO, CreateCategoryContext, Category]
    ): IO[Category] = {
      val (major, minor) = request
      val ctx = CreateCategoryContext(
        UserId(1),
        CategoryName(major),
        CategoryName(minor))

      service(ctx)
    }
  }
  val service = new Service[IO, CreateCategoryContext, Category] {
    override def run(req: CreateCategoryContext): IO[Category] = {
      val group = CategoryGroup(CategoryGroupId(1), UserId(1), CategoryName(req.major.value))
      val category = Category(CategoryId(1), UserId(1), group, CategoryName(req.minor.value))
      IO.pure(category)
    }
  }

   behavior of "Category Service"
   it should "allow me to create a new category" in {
     val food = CategoryName("Food")
     val diningOut = CategoryName("Dining Out")
     val ctx = CreateCategoryContext(UserId(1), food, diningOut)
     val result = service(ctx).unsafeRunSync()
     result.group.name.value should be(food.value)
     result.name.value should be(diningOut.value)

     val pipeline: Service[IO, (String, String), Category] = filter andThen service
     val pipelineResult = pipeline((food.value, diningOut.value)).unsafeRunSync()
     pipelineResult.group.name.value should be(food.value)
     pipelineResult.name.value should be(diningOut.value)
   }

}
