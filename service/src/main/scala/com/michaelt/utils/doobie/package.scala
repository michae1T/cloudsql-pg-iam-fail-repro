package com.michaelt.utils

import _root_.cats.effect.{Async, Resource}
import _root_.doobie.ExecutionContexts
import _root_.doobie.hikari.HikariTransactor
import com.michaelt.config.DBConnectionParameters

package object doobie:
  def transactorResource[F[_]: Async](db: DBConnectionParameters): Resource[F, HikariTransactor[F]] =
    for {
      connectionPool <- ExecutionContexts.fixedThreadPool[F](size = db.connectionPoolSize)
      transactor     <- HikariTransactor.newHikariTransactor[F](
                          "org.postgresql.Driver",
                          db.url,
                          db.user,
                          db.password.getOrElse(""),
                          connectionPool,
                        )
    } yield transactor
