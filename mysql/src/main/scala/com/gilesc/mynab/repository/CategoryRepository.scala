package com.gilesc
package mynab
package repository

trait CategoryGroupRepository[F[_]] {
  def create(ctx: CategoryGroupContext): F[Either[RepositoryError, CategoryGroup]]
  def find(name: CategoryName): F[Option[CategoryGroup]]
}

trait CategoryRepository[F[_]] {
  def create(ctx: CategoryContext): F[Either[RepositoryError, Category]]
  def find(ctx: CategoryContext): F[Option[Category]]
}
