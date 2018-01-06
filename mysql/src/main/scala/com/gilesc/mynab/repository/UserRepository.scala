package com.gilesc
package mynab
package repository

import cats.effect.Async
import cats.syntax.applicativeError._
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.invariant.UnexpectedEnd
import doobie.util.query.Query0
import doobie.util.transactor.Transactor

case class DatabaseConfig(
  driver: String,
  url: String,
  username: String,
  password: String
)

object DatabaseConfig {
  // TODO: This goes away. Just here for getting everything setup
  type UserDTO = (Int, String, String)

  def apply(path: String = "mynab.database"): DatabaseConfig =
    pureconfig.loadConfigOrThrow[DatabaseConfig](path)
}

class Email(val value: String) extends AnyVal
class UserName(val value: String) extends AnyVal
case class User(username: UserName, email: Email)

trait UserRepository[F[_]] {
  def findUser(username: UserName): F[Option[User]]
}

class MysqlUserRepository[F[_] : Async](
  xa: Transactor[F]
) extends UserRepository[F] {
  import DatabaseConfig._

  implicit class UserConversions(dto: UserDTO) {
    def toUser: User = User(
      username = new UserName(dto._2),
      email = new Email(dto._3)
    )
  }

  def userQuery(username: UserName): Query0[UserDTO] = {
    println("SELECT WHERE USERNAME: " + username.value)
    val query = sql"SELECT * FROM api_user WHERE username=${username.value}".query[UserDTO]
    println("QUERY: " + query)
    query
  }

  override def findUser(username: UserName): F[Option[User]] = {
    val userStatement: ConnectionIO[UserDTO] = userQuery(username).unique

    // You might have more than one query involving joins. In such case a for-comprehension would be better
    val program: ConnectionIO[User] = userStatement.map(_.toUser)

    program.map(Option.apply).transact(xa).recoverWith {
      case UnexpectedEnd => Async[F].delay(None) // In case the user is not unique in your db. Check out Doobie's docs.
    }
  }

}

