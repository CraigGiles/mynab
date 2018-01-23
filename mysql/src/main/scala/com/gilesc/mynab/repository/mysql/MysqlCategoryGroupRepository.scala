package com.gilesc
package mynab
package repository
package mysql

import doobie._
import doobie.implicits._

import doobie.util.transactor.Transactor
import doobie.free.connection.ConnectionIO

import cats.effect.Async

import com.gilesc.arrow._

trait CategoryGroupQueries {
  def insertQuery(name: CategoryName): Update0 =
    sql"insert into category_groups (name) values (${name.value})".update

  def findQuery(name: CategoryName): Query0[CategoryGroup] =
    sql"SELECT * FROM category_groups WHERE name=${name}".query[CategoryGroup]
}

class MysqlCategoryGroupRepository[F[_]: Async](
  xa: Transactor[F]
) extends CategoryGroupRepository[F] with CategoryGroupQueries {
  override def create(ctx: CategoryGroupContext) = new MysqlCreateCategoryGroup[F](xa).run(ctx)
  override def find(ctx: CategoryName) = new MysqlFindCategoryGroup[F](xa).run(ctx)
}

class MysqlFindCategoryGroup[F[_]: Async](
  xa: Transactor[F]
) extends Service[F, CategoryName, Option[CategoryGroup]] with CategoryGroupQueries {
  override def run(
    ctx: CategoryName
  ): F[Option[CategoryGroup]] = findQuery(ctx).unique.map(Option.apply).transact(xa)
}

class MysqlCreateCategoryGroup[F[_]: Async](
  xa: Transactor[F]
) extends Service[F, CategoryGroupContext, Either[RepositoryError, CategoryGroup]] with CategoryGroupQueries {
  override def run(
    ctx: CategoryGroupContext
  ): F[Either[RepositoryError, CategoryGroup]] = {
    def insert(name: CategoryName): ConnectionIO[CategoryGroup] = {
      insertQuery(name).withUniqueGeneratedKeys[Long]("ID") map { id =>
        CategoryGroup(CategoryGroupId(id), name)
      }
    }

    insert(ctx.value).transact(xa).attemptSomeSqlState {
      ErrorCode.convert andThen {
        case ErrorCode.DuplicateKey => RepositoryError.DuplicateKey
        case e => RepositoryError.UnknownError(e.toString)
      }
    }
  }
}
