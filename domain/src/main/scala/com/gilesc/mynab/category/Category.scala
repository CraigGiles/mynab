package com.gilesc.mynab.category

import com.gilesc.commons._

case class MajorCategory(value: String) extends AnyVal
case class MinorCategory(value: String) extends AnyVal
case class Category(major: MajorCategory, minor: MinorCategory)

object Category extends CategoryModule with Prepending with Removing {
}
