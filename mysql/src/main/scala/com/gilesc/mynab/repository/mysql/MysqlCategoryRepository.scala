package com.gilesc
package mynab
package repository
package mysql

import doobie._
import doobie.implicits._

import doobie.util.transactor.Transactor
import doobie.free.connection.ConnectionIO

import cats.effect.Async

trait CategoryQueries {
  def insertQuery(
    groupId: CategoryGroupId,
    userId: UserId,
    name: CategoryName
  ): Update0 =
    sql"insert into categories (category_group_id, user_id, name) values (${groupId.value}, ${userId.value}, ${name.value})".update
}

class MysqlCategoryRepository[F[_]: Async](
  xa: Transactor[F]
) extends CategoryRepository[F] with CategoryQueries {
  override def create(
    ctx: CategoryContext
  ): F[Either[RepositoryError, Category]] = {

    def insert(
      userId: UserId,
      name: CategoryName,
      group: CategoryGroup
    ): ConnectionIO[Category] = {
      insertQuery(group.id, userId, name).withUniqueGeneratedKeys[Long]("ID") map { id =>
        Category(CategoryId(id), userId, group, name)
      }
    }

    import java.sql.SQLException
    val result: F[Either[SQLException, Category]] = insert(ctx.userId, ctx.name, ctx.group).transact(xa).attemptSql
    Async[F].map(result)(_.left.map( err => MysqlError.convert(err).repositoryError))
  }
}

