import sbt._
import Keys._

object Dependencies {

  lazy val core = Seq(
    cats
    ,typesafeConfig
    ,scalatest("test")
    ,logging
  )

  lazy val domain = core ++ Seq()
  lazy val backend = core ++ Seq()

  lazy val mysql = Seq(
    cats
    ,scalatest("test")
    ,mysqlConnectorJava
    ,h2database
    ,logbackClassic
    ,doobie("core")
    ,doobie("hikari")
    ,doobie("scalatest")
  )

  lazy val flyway = Seq(
    flywayCore
    ,mysqlConnectorJava)

  lazy val finch = Seq(
    logging
    ,typesafeConfig
    ,finchModules("core")
    ,finchModules("circe")
    ,finchModules("oauth2")
    ,circe("core")
    ,circe("generic")
    ,circe("parser")
    // twitterServer
    // logging
  )

  lazy val akkahttp = Seq(
      logging
      ,akkaHttp("http")
      ,akkaHttp("http-xml")
      ,akkaHttpTestKit()
      ,httpSession
      ,circe("core")
      ,circe("generic")
      ,circe("parser")
      ,akkaHttpCirce
)

  // --------------------
  //   Libraries
  // --------------------
  def typesafeConfig = "com.typesafe" % "config" % "1.3.1"
  def cats = "org.typelevel" %% "cats" % "0.9.0"
  def logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
  def logbackClassic = "ch.qos.logback"  %  "logback-classic" % "1.1.7"
  def scalatest(scope: String) = "org.scalatest" %% "scalatest" % "3.0.1" % scope
  def finchModules(m: String) = "com.github.finagle" %% s"finch-$m" % "0.13.1"
  def circe(m: String) = "io.circe" %% s"circe-$m" % "0.7.0"
  def akkaHttp(m: String) = "com.typesafe.akka" %% s"akka-$m" % "10.0.9"
  def akkaHttpTestKit() = "com.typesafe.akka" %% "akka-http-testkit" % "10.0.9" % Test
  def httpSession = "com.softwaremill.akka-http-session" %% "core" % "0.5.0"
  def akkaHttpCirce = "de.heikoseeberger" %% "akka-http-circe" % "1.17.0"

  // Database
  def flywayCore = "org.flywaydb" % "flyway-core" % "4.0"
  def h2database = "com.h2database"  %  "h2" % "1.4.191"
  def doobie(m: String) = "org.tpolecat" %% s"doobie-$m-cats" % "0.4.1"

  // NOTE: plugins.sbt has the same version of mysql-connector-java.
  //       Please keep these two versions in sync.
  def mysqlConnectorJava = "mysql" % "mysql-connector-java" % "5.1.36"
}
