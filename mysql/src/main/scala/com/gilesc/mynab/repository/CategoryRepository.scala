package com.gilesc
package mynab
package repository

import doobie._
import doobie.implicits._

import doobie.util.transactor.Transactor
import doobie.free.connection.ConnectionIO

import cats._
import cats.data._
import cats.effect._
import cats.implicits._
import cats.effect.Async

import com.gilesc.mynab.repository.mysql._

trait CategoryGroupRepository[F[_]] {
  def create(ctx: CategoryGroupContext): F[Either[PersistenceError, CategoryGroup]]
}

class CategoryGroupRepositoryMysql[F[_]: Async](
  xa: Transactor[F]
) extends CategoryGroupRepository[F] {
  override def create(
    ctx: CategoryGroupContext
  ): F[Either[PersistenceError, CategoryGroup]] = {
    def insert(name: CategoryName): ConnectionIO[CategoryGroup] = for {
        id <- sql"insert into category_groups (name) values (${name.value})".update
          .withUniqueGeneratedKeys[Long]("ID")
      } yield CategoryGroup(CategoryGroupId(id), name)


    insert(ctx.value).transact(xa).attemptSomeSqlState {
       ErrorCode.convert andThen {
           case ErrorCode.DuplicateKey => PersistenceError.DuplicateKey
       }
    }
  }
}

