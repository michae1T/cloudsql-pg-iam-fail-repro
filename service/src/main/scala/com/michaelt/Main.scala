package com.michaelt

import cats.effect.{IO, Resource, ResourceApp}
import config.ServiceConfig
import pureconfig.ConfigSource

object Main extends ResourceApp.Forever:
  def run(args: List[String]): Resource[IO, Unit] =
    for
      serviceConfig <- Resource.eval(IO(ConfigSource.default.loadOrThrow[ServiceConfig]))
      _             <- server.serverResource[IO](serviceConfig.application)
    yield ()
