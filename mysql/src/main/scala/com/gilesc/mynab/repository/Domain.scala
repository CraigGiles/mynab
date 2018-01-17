package com.gilesc
package mynab

package object repository {
  case class CategoryName(value: String) extends AnyVal

  case class CategoryGroupId(value: Long) extends AnyVal
  case class CategoryGroup(id: CategoryGroupId, name: CategoryName)
  case class CategoryGroupContext(value: CategoryName)

  case class CategoryId(value: Long) extends AnyVal
  case class Category(id: CategoryId, group: CategoryGroup, name: CategoryName)
  case class CategoryContext(group: CategoryGroup, name: CategoryName)

  sealed trait RepositoryError

  object RepositoryError {
    final case object DuplicateKey extends RepositoryError
    final case class UnknownError(error: String) extends RepositoryError
  }

}
