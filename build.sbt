name := """Fuyuki-Generation-Sheet"""
organization := "fr.hugov"

version := "1.0-SNAPSHOT"

Compile / scalaSource := baseDirectory.value / "app"
Compile / resourceDirectory := baseDirectory.value / "resources"

enablePlugins(JavaAppPackaging, DockerPlugin)
import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy
dockerChmodType := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown

Docker / maintainer := "hugovedrine@hotmail.com"
Docker / packageName := "fuyuki-gen-sheet"
Docker / version := "1.0"
Docker / daemonUserUid  := None
Docker / daemonUser := "daemon"
Docker / dockerEntrypoint := Seq("[./launch.sh]")
dockerExposedPorts := Seq(9000)
dockerBaseImage := "openjdk:11-jre-slim"
dockerRepository := sys.env.get("ecr_repo")
dockerUpdateLatest := true
dockerEnvVars += ("API_KEY_RAYSHIFT" -> sys.env.get("API_KEY_RAYSHIFT").get)

scalaVersion := "2.13.11"

libraryDependencies += "org.playframework" %% "play-json" % "3.0.5"
libraryDependencies += "org.slf4j" % "slf4j-api" % "2.0.13"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.4.14"
//requests dependency
libraryDependencies += "com.lihaoyi" %% "requests" % "0.8.0"
libraryDependencies += "io.lemonlabs" %% "scala-uri" % "4.0.3"
//Google sheet dependencies
libraryDependencies += "com.google.api-client" % "google-api-client" % "2.0.0"
libraryDependencies += "com.google.oauth-client" % "google-oauth-client-jetty" % "1.34.1"
libraryDependencies += "com.google.http-client" % "google-http-client-jackson2" % "1.43.0"
libraryDependencies += "com.google.apis" % "google-api-services-sheets" % "v4-rev20220927-2.0.0"
libraryDependencies += "com.google.auth" % "google-auth-library-credentials" % "1.11.0"
libraryDependencies += "com.google.auth" % "google-auth-library-oauth2-http" % "1.11.0"
//Google drive dependencies
libraryDependencies += "com.google.apis" % "google-api-services-drive" % "v3-rev20220815-2.0.0"
