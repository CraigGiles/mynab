import sbt._
import Keys._

object Dependencies {

  lazy val core = Seq(
    cats("core")
    ,scalatest("test")
    ,logging
    ,config
  )

  lazy val arrow = core ++ Seq()

  lazy val domain = core ++ Seq(
    bcrypt)
  lazy val service = core ++ Seq()

  lazy val mysql = Seq(
    cats("core")
    ,scalatest("test")
    ,mysqlConnectorJava
    ,h2database
    ,logbackClassic
    ,doobie("core")
    ,doobie("h2")
    ,doobie("hikari")
    ,doobie("scalatest")
  )

  lazy val flyway = Seq(
    flywayCore
    ,mysqlConnectorJava)

  lazy val akkahttp = Seq(
    bcrypt
    ,logging
    ,scalatest("test")
    ,akkaHttp("http")
    ,akkaHttp("http-xml")
    ,akkaHttpTestKit()
    ,httpSession
    ,circe("core")
    ,circe("java8")
    ,circe("generic")
    ,circe("parser")
    ,circe("generic-extras")
    ,akkaHttpCirce
  )

  lazy val testkit = Seq(
    "org.scalatest" %% "scalatest" % "3.0.4",
    "org.scalacheck" %% "scalacheck" % "1.13.4"
  )

  // --------------------
  //   Libraries
  // --------------------
  def cats(m: String) = "org.typelevel" %% s"cats-$m" % "1.0.1"
  def logbackClassic = "ch.qos.logback"  %  "logback-classic" % "1.1.3"
  def logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
  // def config = "com.typesafe" % "config" % "1.3.1"
  def config = "com.github.pureconfig" %% "pureconfig" % "0.8.0"
  def scalatest(scope: String) = "org.scalatest" %% "scalatest" % "3.0.0" % scope
  def finchModules(m: String) = "com.github.finagle" %% s"finch-$m" % "0.13.1"
  def circe(m: String) = "io.circe" %% s"circe-$m" % "0.9.0"

  // Security
  def bcrypt = "org.mindrot" % "jbcrypt" % "0.3m"

  // Web
  def akkaHttp(m: String) = "com.typesafe.akka" %% s"akka-$m" % "10.0.9"
  def akkaHttpTestKit() = "com.typesafe.akka" %% "akka-http-testkit" % "10.0.9" % Test
  def httpSession = "com.softwaremill.akka-http-session" %% "core" % "0.5.0"
  def akkaHttpCirce = "de.heikoseeberger" %% "akka-http-circe" % "1.19.0-M3"

  // Database
  def flywayCore = "org.flywaydb" % "flyway-core" % "4.2.0"
  def h2database = "com.h2database"  %  "h2" % "1.4.191"
  def doobie(m: String) = "org.tpolecat" %% s"doobie-$m" % "0.5.0-M11"

  // NOTE: plugins.sbt has the same version of mysql-connector-java.
  //       Please keep these two versions in sync.
  def mysqlConnectorJava = "mysql" % "mysql-connector-java" % "5.1.36"
}
