package com.michaelt

import cats.{Parallel, ~>}
import cats.data.Kleisli
import cats.effect.{Async, IO, Resource, Spawn}
import cats.syntax.semigroupk.toSemigroupKOps
import com.michaelt.config.ApplicationConfig
import com.michaelt.db.Repository
import com.michaelt.utils.doobie.transactorResource
import fs2.Stream
import trace4cats.Span
import trace4cats.http4s.server.syntax.TracedRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Server
import org.http4s.server.middleware.GZip
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.syntax.LoggerInterpolator

package object server:
  def serverResource[F[*]: Async: Parallel](applicationConfig: ApplicationConfig): Resource[F, Server] =

    given Logger[F] = Slf4jLogger.getLogger[F]

    for
      transactor                         <- transactorResource[F](applicationConfig.db.toConnectionParameters)
      notificationConfigurationRepository = Repository[F](transactor)
      service                             = Service[F](notificationConfigurationRepository)
      httpApp                             = service.httpRoutes.orNotFound
      server                             <- BlazeServerBuilder[F]
                                              .bindHttp(applicationConfig.port, "0.0.0.0")
                                              .withHttpApp(httpApp)
                                              .resource
      _                                  <- Spawn[F].background[Nothing](
                                              Stream.awakeEvery[F](applicationConfig.heartbeatPeriod)
                                                .evalMap(_ => info"Heartbeat")
                                                .holdOptionResource
                                                .useForever
                                            )
    yield server
