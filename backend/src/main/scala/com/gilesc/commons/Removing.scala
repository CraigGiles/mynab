package com.gilesc.commons

/**
  * Created by craiggiles on 2/11/17.
  */
trait Removing {
  def remove[T, F[_]]: (T, Vector[T]) => Vector[T] = (t, s) => s.filterNot(tr => tr == t)

}
