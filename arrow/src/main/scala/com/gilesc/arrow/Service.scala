package com.gilesc
package arrow

import scala.concurrent.Future

abstract class Service[-Req, +Resp] extends Function1[Req, Future[Resp]] {
  override def apply(request: Req): Future[Resp]
}
