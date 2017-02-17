package com.gilesc.commons

trait Removing {
  def remove[T]: (T, Vector[T]) => Vector[T] = (t, s) => s.filterNot(tr => tr == t)
}
