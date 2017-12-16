package com.gilesc
package mynab
package authentication

import com.gilesc.mynab.UniqueId
import java.time.OffsetDateTime

case class PasswordResetToken(token: UniqueId, expires: OffsetDateTime)
