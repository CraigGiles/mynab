package com.gilesc
package arrow

import scala.concurrent.Future

abstract class Filter[-ReqIn, +RepOut, +ReqOut, -RepIn]
    extends ((ReqIn, Service[ReqOut, RepIn]) => Future[RepOut])

trait SimpleFilter[ReqIn, ReqOut] extends Filter[ReqIn, ReqOut, ReqIn, ReqOut]
