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
    name: CategoryName,
    groupId: CategoryGroupId
  ): Update0 =
    sql"insert into categories (category_group_id, name) values (${groupId.value}, ${name.value})".update
}

class MysqlCategoryRepository[F[_]: Async](
  xa: Transactor[F]
) extends CategoryRepository[F] with CategoryQueries {
  override def create(
    ctx: CategoryContext
  ): F[Either[RepositoryError, Category]] = {

    def insert(
      name: CategoryName,
      group: CategoryGroup
    ): ConnectionIO[Category] = {
      insertQuery(name, group.id).withUniqueGeneratedKeys[Long]("ID") map { id =>
        Category(CategoryId(id), group, name)
      }
    }

    insert(ctx.name, ctx.group).transact(xa).attemptSomeSqlState {
       ErrorCode.convert andThen {
           case ErrorCode.DuplicateKey => RepositoryError.DuplicateKey
           case e => RepositoryError.UnknownError(e.toString)
       }
    }
  }
}

