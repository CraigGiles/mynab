package com.gilesc
package mynab
package persistence
package mysql

final case class PersistedId(value: Long) extends AnyVal

trait Persisted[T] {
  def id: PersistedId
  def value: T
}
