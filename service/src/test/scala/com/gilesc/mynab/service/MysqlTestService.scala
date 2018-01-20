// package com.gilesc
// package mynab
// package service

// import doobie.scalatest._
// import doobie.util.transactor.Transactor

// import cats.effect.IO

// import com.gilesc.mynab.testkit.TestCase

// import com.gilesc.mynab.repository.mysql._
// import com.gilesc.mynab.testkit.DatabaseTestCase
// import com.gilesc.mynab.testkit.TestCase
// import com.gilesc.mynab.repository._
// import cats.effect.IO
// import cats.data.EitherT
// import cats.syntax.either._

// class MysqlCategoryRepositorySpec extends DatabaseTestCase {
//     case class DatabaseConfig(
//       driver: String,
//       url: String,
//       username: String,
//       password: String
//     )
//     val config = pureconfig.loadConfigOrThrow[DatabaseConfig]("mynab.database")

//   def transactor = Transactor.fromDriverManager[IO](
//     config.driver,
//     config.url,
//     config.username,
//     config.password)

//   private val groupRepo = new MysqlCategoryGroupRepository[IO](transactor)
//   private val categoryRepo = new MysqlCategoryRepository[IO](transactor)


//   behavior of "Category Service With MySQL"
//   it should "use the mysql store correctly" in {
//     val service = CreateCategoryService.apply[IO](groupRepo, categoryRepo)
//     val food = CategoryName("Food")
//     val diningOut = CategoryName("Dining Out")
//     val ctx = CreateCategoryContext(food, diningOut)
//     val result = service(ctx).unsafeRunSync
//     println()
//     println()
//     println(result)
//     println()
//     println()
//   }

// //   it should "allow me to store a category group" in {
// //     val name = CategoryName("hello world")
// //     val ctx = new CategoryGroupContext(name)
// //     val Right(result) = repo.create(ctx).unsafeRunSync
// //     result should be(CategoryGroup(CategoryGroupId(1), name))
// //   }

// //   it should "give me a duplicate key error when inserting the same group name" in {
// //     val name = CategoryName("mycategoryname")
// //     val ctx = new CategoryGroupContext(name)
// //     val Right(first) = repo.create(ctx).unsafeRunSync
// //     val Left(second) = repo.create(ctx).unsafeRunSync

// //     first.name should be(name)
// //     second should be(RepositoryError.DuplicateKey)
// //   }

// //   behavior of "Category Repository"
// //   it should "allows me to store a category" in {
// //     val catRepo = new MysqlCategoryRepository[IO](transactor)
// //     val name = CategoryName("Food")
// //     val ctx = new CategoryGroupContext(name)
// //     val Right(group) = repo.create(ctx).unsafeRunSync

// //     val categoryName = CategoryName("Groceries")
// //     val catContext = new CategoryContext(group, categoryName)
// //     val category = catRepo.create(catContext).unsafeRunSync
// //   }

// }


