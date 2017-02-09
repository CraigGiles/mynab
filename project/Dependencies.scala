import sbt._
import Keys._

object Dependencies {
  val scope = "test"

  // Projects
  // --------------------
  lazy val core = Seq(
    cats,
    scalatest(scope),
    logging
  )

  lazy val flyway = Seq(
      flywayCore,
      mysqlConnectorJava)

  lazy val http4s = core ++ Seq(
    "org.http4s" %% "http4s-blaze-server" % "0.15.3a",
    "org.http4s" %% "http4s-dsl"          % "0.15.3a",
    "org.http4s" %% "http4s-argonaut"     % "0.15.3a")

  lazy val backend = core ++ Seq(
      "org.javamoney" % "moneta" % "1.1",
      mysqlConnectorJava)

  // Libraries
  // --------------------
  // Category Theory
  def cats = "org.typelevel" %% "cats" % "0.9.0"

  // Database
  def flywayCore = "org.flywaydb" % "flyway-core" % "4.0"

  // NOTE: plugins.sbt has the same version of mysql-connector-java.
  //       Please keep these two versions in sync.
  def mysqlConnectorJava = "mysql" % "mysql-connector-java" % "5.1.36"

  def logging = "org.slf4j" % "slf4j-simple" % "1.6.4"
  def scalatest(scope: String) = "org.scalatest" %% "scalatest" % "3.0.0" % scope
}
