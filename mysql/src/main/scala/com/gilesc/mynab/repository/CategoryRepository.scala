package com.gilesc
package mynab
package repository

trait CategoryGroupRepository[F[_]] {
  def create(ctx: CategoryGroupContext): F[Either[RepositoryError, CategoryGroup]]
}

trait CategoryRepository[F[_]] {
  def create(ctx: CategoryContext): F[Either[RepositoryError, Category]]
}
