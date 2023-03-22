package com.michaelt.db

import cats.Applicative
import cats.data.{EitherT, NonEmptySet, OptionT}
import cats.effect.Sync
import cats.syntax.applicativeError.catsSyntaxApplicativeError
import cats.syntax.apply.catsSyntaxApply
import cats.syntax.either.catsSyntaxEitherId
import cats.syntax.flatMap.toFlatMapOps
import cats.syntax.functor.toFunctorOps
import cats.syntax.set.catsSyntaxSet
import diffson.jsonpatch.JsonPatch
import doobie.postgres.implicits.UuidType
import doobie.syntax.all.{toConnectionIOOps, toSqlInterpolator}
import doobie.util.fragments
import doobie.{ConnectionIO, Transactor}
import io.circe.{Decoder, Encoder, Json}
import org.postgresql.util.{PSQLException, PSQLState}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.syntax.LoggerInterpolator
import trace4cats.Trace

import java.util.UUID
import scala.collection.immutable.SortedSet

class Repository[F[*]: Sync](transactor: Transactor[F]):
  def get(): F[Option[String]] =
    NotificationConfigurationQueries.get()
      .transact(transactor)

end Repository

object NotificationConfigurationQueries:

  def get(
  ): ConnectionIO[Option[String]] =
    sql.select().option

  private[db] object sql:
    def select(
    ) =
      sql"""
        SELECT 'hello'
      """
        .query[String]

end NotificationConfigurationQueries
