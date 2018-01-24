package com.gilesc
package mynab

package object repository {
  sealed trait RepositoryError

  object RepositoryError {
    final case object DuplicateKey extends RepositoryError
    final case object ForeignKeyConstraint extends RepositoryError
    final case class UnknownError(error: String) extends RepositoryError
  }

}
