package com.gilesc
package mynab
package finch
package presenter

trait Presenter[T, U] {
  def present(in: T): U
}
