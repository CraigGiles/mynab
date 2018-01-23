package com.gilesc
package arrow

import cats.Monad
import cats.data.Kleisli

abstract class Filter[F[_]: Monad, ReqIn, RepOut, ReqOut, RepIn] {
  /**
    * The logic needed to run your filter goes here
    */
  def run(
    in: ReqIn,
    service: Service[F, ReqOut, RepIn]
  ): F[RepOut]

  /**
    * Apply method for using your value as a function. This will turn your "run"
    * function into a Kleisli so you can chain them using the 'andThen' construct
    */
  def apply(): Kleisli[F, (ReqIn, Service[F, ReqOut, RepIn]), RepOut] = Kleisli {
    r: (ReqIn, Service[F, ReqOut, RepIn]) =>
      val (in, service) = r
      run(in, service)
  }

  /**
    * executes a filter after this current filter keeping the chain alive
    */
  def andThen[NextIn, NextOut](
    next: Filter[F, ReqOut, RepIn, NextIn, NextOut]
  ): Filter[F, ReqIn, RepOut, NextIn, NextOut] = {
    new Filter[F, ReqIn, RepOut, NextIn, NextOut] {
      override def run(
        reqIn: ReqIn,
        service: Service[F, NextIn, NextOut]
      ): F[RepOut] = {
        val svc = new Service[F, ReqOut, RepIn] {
          override def run(req: ReqOut): F[RepIn] = next.run(req, service)
        }

        Filter.this.run(reqIn, svc)
      }
    }
  }

  /**
    * Terminates a filter chain in a service.
    */
  def andThen(
    service: Service[F, ReqOut, RepIn]
  ): Service[F, ReqIn, RepOut] = new Service[F, ReqIn, RepOut] {
    override def run(r: ReqIn): F[RepOut] = {
      Filter.this.run(r, service)
    }
  }
}

abstract class SimpleFilter[F[_]: Monad, Req, Rep] extends Filter[F, Req, Rep, Req, Rep]

object Filter {
  def identity[F[_]: Monad, Req, Rep] = new SimpleFilter[F, Req, Rep] {
    override def run(
      reqIn: Req,
      service: Service[F, Req, Rep]
    ): F[Rep] = service(reqIn)
  }
}
