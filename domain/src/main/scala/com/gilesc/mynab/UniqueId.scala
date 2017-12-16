package com.gilesc
package mynab

import java.util.UUID
import scala.util.Try

case class UniqueId(value: UUID)

object UniqueId {
  def fromString(value: String): Option[UniqueId] =
    Try(new UniqueId(UUID.fromString(value))).toOption

  def random() = UniqueId(UUID.randomUUID)
}
