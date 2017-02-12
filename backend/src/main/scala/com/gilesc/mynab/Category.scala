package com.gilesc.mynab
package category

case class MajorCategory(value: String) extends AnyVal
case class MinorCategory(value: String) extends AnyVal
case class Category(major: MajorCategory, minor: MinorCategory)

