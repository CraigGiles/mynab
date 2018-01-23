package com.gilesc

import cats.Monad
import cats.data.Kleisli

package object arrow {
  implicit def convertAndThen[M[_]: Monad, A, B](
    service: Service[M, A, B]
  ): Kleisli[M, A, B] = service.apply()

  implicit def convertFilterAndThen[M[_]: Monad, A, B, C, D](
    filter: Filter[M, A, B, C, D]
  ): Kleisli[M, (A, Service[M, C, D]), B] = filter.apply()
}
