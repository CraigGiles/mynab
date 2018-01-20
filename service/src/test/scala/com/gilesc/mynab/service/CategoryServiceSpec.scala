package com.gilesc
package mynab
package service

import com.gilesc.mynab.testkit.TestCase
import com.gilesc.mynab.repository._
import cats.effect.IO
import cats.data.EitherT
import cats.syntax.either._

object IOAssertion {
  def apply[A](ioa: IO[A]): A = ioa.unsafeRunSync()
}

// object TestUserService {
//   private val testUserRepo: UserRepository[IO] =
//     (username: UserName) => IO {
//       users.find(_.username.value == username.value)
//     }

//   val service: UserService[IO] = new UserService[IO](testUserRepo)
// }

class CategoryServiceSpec extends TestCase {
  // def categories: CreateCategoryContext => IO[Either[RepositoryError, Category]] =
  //   ctx => IO.pure(Left(RepositoryError.DuplicateKey))

  // val groups = new CategoryGroupRepository[IO] {
  //   override def create(
  //     ctx: CategoryGroupContext
  //   ): IO[Either[RepositoryError, CategoryGroup]] = {
  //     IO.pure(Left(RepositoryError.DuplicateKey))
  //   }
  // }

  // val categories = new CategoryRepository[IO] {
  //   override def create(
  //     ctx: CategoryContext
  //   ): IO[Either[RepositoryError, Category]] = {
  //     IO.pure(Left(RepositoryError.DuplicateKey))
  //   }
  // }


  // behavior of "Category Service"
  // it should "allow me to create a new category" in {
  //   val service = CreateCategoryService.apply[IO](groups, categories)
  //   val food = CategoryName("Food")
  //   val diningOut = CategoryName("Dining Out")
  //   val ctx = CreateCategoryContext(food, diningOut)
  //   val Left(result) = service(ctx).unsafeRunSync
  //   result shouldBe a [RepositoryError.DuplicateKey.type]
  // }

}
