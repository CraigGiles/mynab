package com.gilesc
package mynab
package service

import cats.data.EitherT
import cats.effect.Async

import com.gilesc.arrow.Service
import com.gilesc.mynab.repository.RepositoryError
import com.gilesc.mynab.repository.CategoryGroupRepository
import com.gilesc.mynab.repository.CategoryRepository

case class CreateCategoryContext(major: CategoryName, minor: CategoryName)

final class CreateCategoryService[F[_]: Async](
    createGroup: Service[F, CategoryGroupContext, Either[RepositoryError, CategoryGroup]] ,
    findGroup: Service[F, CategoryName, Option[CategoryGroup]],
    createCategory: Service[F, CategoryContext, Either[RepositoryError, Category]]
  ) extends Service[F, CreateCategoryContext, Either[String, Category]] {

  private[this] def getGroupFor(
    name: CategoryName
  ): EitherT[F, RepositoryError, CategoryGroup] = {
    EitherT(createGroup(CategoryGroupContext(name))).leftFlatMap {
      case RepositoryError.DuplicateKey =>
        EitherT.fromOptionF(findGroup(name), RepositoryError.DuplicateKey)
      case error => EitherT(Async[F].delay(Left(error)))
    }
  }

  // TODO: change the String to a real ADT
  override def run(
    ctx: CreateCategoryContext
  ): F[Either[String, Category]] = {
    val result = for {
      group <- getGroupFor(ctx.major)
      category <- EitherT(createCategory(CategoryContext(group, ctx.minor)))
    } yield category

    result.leftMap(_.toString).value
  }

}

object CreateCategoryService {
  def apply[F[_]: Async](
    groups: CategoryGroupRepository[F],
    categories: CategoryRepository[F]
  ): CreateCategoryService[F] = {
    new CreateCategoryService[F](
      Service(groups.create),
      Service(groups.find),
      Service(categories.create)
    )
  }
}
