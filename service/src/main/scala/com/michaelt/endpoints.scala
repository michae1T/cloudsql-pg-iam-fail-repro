package com.michaelt

import cats.data.NonEmptyList
import sttp.model.StatusCode
import sttp.tapir.generic.auto.*
import sttp.tapir.integ.cats.codec.*
import sttp.tapir.json.circe.*
import sttp.tapir.typelevel.ParamConcat
import sttp.tapir.{EndpointInput, PublicEndpoint, Schema, Validator, endpoint, path, statusCode, stringToPath}

import java.util.UUID

object endpoints:
  private val base: PublicEndpoint[Unit,StatusCode, Unit, Any] =
    endpoint.errorOut(statusCode)

  val select: PublicEndpoint[Unit, StatusCode, Option[String], Any] =
    base.in("select").get.out(jsonBody[Option[String]])

