package com.gilesc
package arrow

import cats.Monad
import cats.data.Kleisli

abstract class Service[F[_]: Monad, Req, Resp] {
  def apply(): Kleisli[F, Req, Resp] = Kleisli.apply(run)
  def run(req: Req): F[Resp]

  def andThen[C](
    service: Service[F, Resp, C]
  ): Kleisli[F, Req, C] = this.apply().andThen(service.run _)

}

object Service {
  def apply[F[_]: Monad, Req, Resp](f: Req => F[Resp]): Service[F, Req, Resp] = {
    new Service[F, Req, Resp] {
      override def run(req: Req) = f(req)
    }
  }
}

