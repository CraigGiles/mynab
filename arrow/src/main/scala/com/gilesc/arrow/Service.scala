package com.gilesc
package arrow

import cats.Monad

abstract class Service[F[_]: Monad, Req, Resp] extends Function1[Req, F[Resp]] {
  override def apply(request: Req): F[Resp]

  override def andThen[A](g: F[Resp] => A): Req => A = { req: Req =>
    val resp: F[Resp] = apply(req)
    val nextResp: A = g(resp)
    nextResp
  }

  def pipeTo[C](next: Resp => F[C]): Req => F[C] = { req: Req =>
    val fTo = Monad[F].lift(next)
    val nextApplied = fTo(apply(req))
    Monad[F].flatten(nextApplied)
  }

}

