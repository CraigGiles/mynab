package com.gilesc.commons

/**
  * Created by craiggiles on 2/11/17.
  */
trait Prepending {
  def prepend[T, F[_]]: (T, Vector[T]) => Vector[T] = (t, s) => t +: s
}
