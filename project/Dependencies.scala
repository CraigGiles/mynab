import sbt._
import Keys._

object Dependencies {
  val scope = "test"

  lazy val core = Seq(
    cats,
    scalatest(scope),
    logging
  )

  lazy val domain = core ++ Seq()
  lazy val backend = core ++ Seq(
    scalalikeJDBCTest("bt"))

  lazy val mysql = Seq(
    "com.typesafe.slick" %% "slick" % "3.2.0",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0",
    cats,
    scalatest(scope),
    mysqlConnectorJava,
    h2database,
    logbackClassic)

  lazy val flyway = Seq(
      flywayCore,
      mysqlConnectorJava)

  lazy val finch = Seq(
    logging,
    finchModules("core"),
    finchModules("circe"),
    finchModules("oauth2"),
    circe("core"),
    circe("generic"),
    circe("parser")
    // twitterServer
    // logging
  )

  // --------------------
  //   Libraries
  // --------------------
  def cats = "org.typelevel" %% "cats" % "0.9.0"
  def logging = "org.slf4j" % "slf4j-simple" % "1.6.4"
  def scalatest(scope: String) = "org.scalatest" %% "scalatest" % "3.0.0" % scope
  def finchModules(m: String) = "com.github.finagle" %% s"finch-$m" % "0.13.1"
  def circe(m: String) = "io.circe" %% s"circe-$m" % "0.7.0"

  // Database
  def flywayCore = "org.flywaydb" % "flyway-core" % "4.0"
  def scalalikeJDBC(module: String) = "org.scalikejdbc" %% s"scalikejdbc$module" % "2.5.1"
  def h2database = "com.h2database"  %  "h2" % "1.4.191"
  def logbackClassic = "ch.qos.logback"  %  "logback-classic" % "1.1.3"
  def scalalikeJDBCTest(scope: String) = scalalikeJDBC("-test") % scope

  // NOTE: plugins.sbt has the same version of mysql-connector-java.
  //       Please keep these two versions in sync.
  def mysqlConnectorJava = "mysql" % "mysql-connector-java" % "5.1.36"
}
