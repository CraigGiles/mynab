package com.gilesc
package mynab
package service

import cats.data.EitherT
import cats.effect.Async

import com.gilesc.arrow.Service
import com.gilesc.mynab.repository.RepositoryError
import com.gilesc.mynab.repository.CategoryGroupRepository
import com.gilesc.mynab.repository.CategoryRepository

sealed abstract class ServiceError(val message: String, val throwable: Throwable = null)
object ServiceError {
  final case class UnknownUser(userId: UserId) extends ServiceError(s"User id ${userId.value} doesn't exist.")
  final case class UnknownError(override val message: String) extends ServiceError(message)
}

case class CreateCategoryContext(user: UserId, major: CategoryName, minor: CategoryName)

final class CreateCategoryService[F[_]: Async](
    createGroup: Service[F, CategoryGroupContext, Either[RepositoryError, CategoryGroup]] ,
    findGroup: Service[F, CategoryName, Option[CategoryGroup]],
    createCategory: Service[F, CategoryContext, Either[RepositoryError, Category]]
  ) extends Service[F, CreateCategoryContext, Either[ServiceError, Category]] {

  /**
    * Create and return the CategoryGroup. If the group already exists,
    * attempt to find that group's id and use that instead.
    */
  private[this] def getGroupFor(
    user: UserId,
    name: CategoryName
  ): EitherT[F, RepositoryError, CategoryGroup] = {
    EitherT(createGroup(CategoryGroupContext(user, name))).leftFlatMap {
      case RepositoryError.DuplicateKey =>
        EitherT.fromOptionF(findGroup(name), RepositoryError.DuplicateKey)
      case error => EitherT(Async[F].delay(Left(error)))
    }
  }

  // TODO: Add a `find` for the category
  override def run(
    ctx: CreateCategoryContext
  ): F[Either[ServiceError, Category]] = {
    val result = for {
      group <- getGroupFor(ctx.user, ctx.major)
      category <- EitherT(createCategory(CategoryContext(ctx.user, group, ctx.minor)))
    } yield category

    // Convert the RepositoryError to the appropriate ServiceError
    val eitherT: EitherT[F, ServiceError, Category] = result.leftMap {
      case RepositoryError.ForeignKeyConstraint => ServiceError.UnknownUser(ctx.user)
      case a => ServiceError.UnknownError(a.toString)
    }

    eitherT.value
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
