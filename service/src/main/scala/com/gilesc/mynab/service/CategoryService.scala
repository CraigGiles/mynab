package com.gilesc
package mynab
package service

import cats.data.EitherT
import cats.implicits._
import cats.effect.Async
import cats.syntax.functor._

import com.gilesc.mynab.repository._
import com.gilesc.mynab.repository.CategoryName
import com.gilesc.mynab.repository.CategoryGroupRepository
import com.gilesc.mynab.repository.CategoryRepository

case class CreateCategoryContext(major: CategoryName, minor: CategoryName)


final class CreateCategoryService[F[_]: Async](
    groups: CategoryGroupRepository[F],
    categories: CategoryRepository[F]
  ) {
    def apply(ctx: CreateCategoryContext): F[Either[String, Category]] = {
      val result = for {
        group <- EitherT(groups.create(CategoryGroupContext(ctx.major)))
        category <- EitherT(categories.create(CategoryContext(group, ctx.minor)))
      } yield category

      result.leftMap(_.toString).value
    }
}

object CreateCategoryService {
  def apply[F[_]: Async](
    groups: CategoryGroupRepository[F],
    categories: CategoryRepository[F]
  ): CreateCategoryService[F] = {
    new CreateCategoryService[F](groups, categories)
  }
}
