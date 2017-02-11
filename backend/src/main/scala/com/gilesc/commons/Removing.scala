package com.gilesc.commons

/**
  * Created by craiggiles on 2/11/17.
  */
trait Removing {
  def remove[T, F[_]]: (T, List[T]) => List[T] = (t, s) => s.filterNot(tr => tr == t)

}
