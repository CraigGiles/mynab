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
  lazy val backend = core ++ Seq()

  lazy val mysql = core ++ Seq(mysqlConnectorJava)
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

  // NOTE: plugins.sbt has the same version of mysql-connector-java.
  //       Please keep these two versions in sync.
  def mysqlConnectorJava = "mysql" % "mysql-connector-java" % "5.1.36"
}
