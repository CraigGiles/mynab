package com.gilesc
package arrow

import cats.Monad

abstract class Filter[F[_]: Monad, ReqIn, RepOut, ReqOut, RepIn]
  extends ((ReqIn, Service[F, ReqOut, RepIn]) => F[RepOut])

trait SimpleFilter[F[_], ReqIn, ReqOut] extends Filter[F, ReqIn, ReqOut, ReqIn, ReqOut]





