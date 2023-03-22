package com.michaelt

import cats.effect.Async
import cats.syntax.either.catsSyntaxEitherId
import cats.syntax.functor.toFunctorOps
import com.michaelt.db.Repository
import org.http4s.HttpRoutes
import sttp.model.StatusCode
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import trace4cats.Trace

class Service[F[*]: Async](
  notificationConfigurationRepository: Repository[F],
):
  def httpRoutes: HttpRoutes[F] =
    Http4sServerInterpreter[F]().toRoutes(allServerEndpoints)


  private val allServerEndpoints: List[ServerEndpoint[Any, F]] = List(
    endpoints.select.serverLogic(
      _ => notificationConfigurationRepository.get().map(Right.apply)
    ),
  )

