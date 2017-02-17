package com.gilesc.commons

trait Prepending {
  def prepend[T]: (T, Vector[T]) => Vector[T] = (t, s) => t +: s
}
