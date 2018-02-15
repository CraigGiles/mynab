package com.gilesc
package mynab
package repository
package mysql

import com.gilesc.arrow._

import doobie._
import doobie.implicits._

import doobie.util.transactor.Transactor
import doobie.free.connection.ConnectionIO

import cats.effect.Async

case class CategoryRow(id: CategoryId, groupId: CategoryGroupId, userId: UserId, name: CategoryName, groupName: CategoryName)

trait CategoryQueries {
  def insertQuery(
    groupId: CategoryGroupId,
    userId: UserId,
    name: CategoryName
  ): Update0 =
    sql"insert into categories (category_group_id, user_id, name) values (${groupId.value}, ${userId.value}, ${name.value})".update

  def findQuery(userId: UserId, groupId: CategoryGroupId, name: CategoryName): Query0[CategoryRow] =
    sql"""
         SELECT
           categories.id,
           categories.category_group_id as groupId,
           categories.user_id as userId,
           categories.name as name,
           category_groups.name as groupName
         FROM categories
           JOIN category_groups ON categories.category_group_id=category_groups.id
         WHERE categories.name=${name}
         AND categories.category_group_id=${groupId}
         AND categories.user_id=${userId};
    """.query[CategoryRow]
}

class MysqlCategoryRepository[F[_]: Async](
  xa: Transactor[F]
) extends CategoryRepository[F] with CategoryQueries {
  override def create(ctx: CategoryContext) = new MysqlCreateCategoryService[F](xa).run(ctx)
  override def find(ctx: CategoryContext) = new MysqlFindCategoryService[F](xa).run(ctx)
}

final class MysqlCreateCategoryService[F[_]: Async](
  xa: Transactor[F]
) extends Service[F, CategoryContext, Either[RepositoryError, Category]] with CategoryQueries {
  override def run(ctx: CategoryContext): F[Either[RepositoryError, Category]] = {

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
    val result: F[Either[SQLException, Category]] =
      insert(ctx.userId, ctx.name, ctx.group).transact(xa).attemptSql
    Async[F].map(result)(_.left.map( err => MysqlError.convert(err).repositoryError))
  }
}


final class MysqlFindCategoryService[F[_]: Async](
  xa: Transactor[F]
) extends Service[F, CategoryContext, Option[Category]] with CategoryQueries {
  override def run(ctx: CategoryContext): F[Option[Category]] = {
    val found = findQuery(ctx.userId, ctx.group.id, ctx.name).unique.map(Option.apply).transact(xa)
    Async[F].map(found) { _.map { row =>
        val group = CategoryGroup(row.groupId, row.userId, row.groupName)
        Category(row.id, row.userId, group, row.name)
      }
    }
  }
}

