package com.gilesc
package mynab
package service

import cats.data.EitherT
import cats.effect.Async
import com.gilesc.mynab.repository._
import com.gilesc.mynab.repository.CategoryName
import com.gilesc.mynab.repository.CategoryGroupRepository
import com.gilesc.mynab.repository.CategoryRepository

case class CreateCategoryContext(major: CategoryName, minor: CategoryName)

final class CreateCategoryService[F[_]: Async](
    groups: CategoryGroupRepository[F],
    categories: CategoryRepository[F]
  ) {
    private[this] def getGroupFor(
      name: CategoryName
    ): EitherT[F, RepositoryError, CategoryGroup] = {
      EitherT(groups.create(CategoryGroupContext(name))).leftFlatMap {
        case RepositoryError.DuplicateKey =>
          EitherT.fromOptionF(groups.find(name), RepositoryError.DuplicateKey)
        case error => EitherT(Async[F].delay(Left(error)))
      }
    }

    // TODO: change the String to a real ADT
    def apply(ctx: CreateCategoryContext): F[Either[String, Category]] = {
      val result = for {
        group <- getGroupFor(ctx.major)
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
