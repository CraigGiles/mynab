package com.gilesc

import cats.Monad

package object arrow {
  implicit def convertPipeTo[M[_]: Monad, A, B](f: (A => M[B])): Service[M, A, B] = {
    new Service[M, A, B] {
      override def apply(request: A) = f(request)
    }
  }

  implicit def convertAndThen[M[_]: Monad, A, B](service: Service[M, A, B]): M[A] => M[B] = { ma: M[A] =>
    val function: A => M[B] = service.apply
    val mma = Monad[M].lift(function)(ma)
    Monad[M].flatten(mma)
  }
}
