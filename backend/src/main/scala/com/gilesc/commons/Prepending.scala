package com.gilesc.commons

/**
  * Created by craiggiles on 2/11/17.
  */
trait Prepending {
  def prepend[T, F[_]]: (T, List[T]) => List[T] = (t, s) => t :: s
}
