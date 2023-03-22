package com.michaelt

import pureconfig.ConfigReader
import pureconfig.generic.derivation.EnumConfigReader
import pureconfig.generic.derivation.default.*

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import scala.concurrent.duration.FiniteDuration

object config:
  case class ServiceConfig(application: ApplicationConfig) derives ConfigReader

  case class ApplicationConfig(
    port: Int,
    db: DBConnectionConfig,
    heartbeatPeriod: FiniteDuration,
  )

  case class DBConnectionConfig(
    connectionType: DBConnectionType,
    host: String,
    dbName: String,
    port: Int,
    user: String,
    password: Option[String],
    cloudSqlInstance: Option[String],
    autosave: Option[PostgresAutosaveSetting],
    connectionPoolSize: Int,
  ) {
    val toConnectionParameters = {
      val dbNameEncoded = URLEncoder.encode(dbName, StandardCharsets.UTF_8.toString)
      val cloudSqlInstanceParameter =
        cloudSqlInstance
          .map(URLEncoder.encode(_, StandardCharsets.UTF_8))
          .map(parameter => s"cloudSqlInstance=$parameter&")
          .getOrElse("")
      val autosaveParameter = autosave.map(parameter => s"autosave=${parameter.toString.toLowerCase}&").getOrElse("")
      val connectionTypeParameters =
        if connectionType == DBConnectionType.GCP then
          s"socketFactory=com.google.cloud.sql.postgres.SocketFactory&" +
            s"enableIamAuth=true&" +
            s"sslmode=disable"
        else
          ""

      val url =
        s"jdbc:postgresql://$host:$port/$dbNameEncoded?$autosaveParameter$cloudSqlInstanceParameter$connectionTypeParameters"

      DBConnectionParameters(url, user, password, connectionPoolSize)
    }
  }

  case class DBConnectionParameters(url: String, user: String, password: Option[String], connectionPoolSize: Int)

  enum PostgresAutosaveSetting derives EnumConfigReader:
    case Always, Conservative, Never

  enum DBConnectionType derives EnumConfigReader:
    case GCP, Standard
