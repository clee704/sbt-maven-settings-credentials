// Copyright (C) 2024 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package dev.chungmin.sbt

import java.net.URL
import scala.collection.JavaConverters._
import scala.util.control.NonFatal
import scala.xml.XML

import sbt._
import sbt.internal.util.ManagedLogger
import Keys._

object MavenSettingsCredentialsPlugin extends AutoPlugin {
  override def trigger = allRequirements

  override lazy val projectSettings = Seq(
    credentials ++= buildCredentials(externalResolvers.value, streams.value.log)
  )

  private def buildCredentials(resolvers: Seq[Resolver], log: ManagedLogger): Seq[Credentials] = {
    // TODO: support M2_HOME
    val settingsFile = new File(sbt.io.Path.userHome, ".m2/settings.xml")
    if (!settingsFile.exists) {
      return Nil
    }
    val xml = XML.loadFile(settingsFile)
    val servers = (xml \ "servers" \ "server").map { server =>
      val id = (server \ "id").text
      val username = (server \ "username").text
      val password = (server \ "password").text
      (id -> (username, password))
    }.toMap
    resolvers.map {
      case resolver: MavenRepo =>
        servers.get(resolver.name).map { server =>
          val url = new URI(resolver.root).toURL
          val host = url.getHost
          val realm = try {
            val conn = url.openConnection()
            conn.getHeaderFields.asScala.get("WWW-Authenticate").flatMap { values =>
              values.asScala.find(_.startsWith("Basic realm=")).flatMap { value =>
                // TODO proper parsing
                "Basic realm=\"([^\"]*)\"".r.findFirstMatchIn(value).map { m =>
                  m.group(1)
                }
              }
            }.getOrElse("")
          } catch {
            case NonFatal(e) =>
              log.warn(s"Failed to get realm for host $host: $e")
              ""
          }
          Credentials(realm, host, server._1, server._2)
        }
      case _ => None
    }.flatten
  }
}
