package com.gilesc
package mynab
package repository
package mysql

import com.spotify.docker.client._
import com.whisk.docker._


trait DockerMysqlService extends DockerKit {
  def MysqlAdvertisedPort = 3306
  val MysqlUser = "root"
  val MysqlPassword = "root"
  val MysqlDatabase = "typelevel"

  val mysqlContainer = DockerContainer("mysql:5.7.14")
    .withPorts((MysqlAdvertisedPort, None))
    .withEnv(s"MYSQL_USER=$MysqlUser", s"MYSQL_ROOT_PASSWORD=$MysqlPassword", s"MYSQL_DATABASE=$MysqlDatabase")
    .withReadyChecker(
      DockerReadyChecker
        .LogLineContains("MySQL init process done. Ready for start up.")
    )

  abstract override def dockerContainers: List[DockerContainer] =
    mysqlContainer :: super.dockerContainers
}

//trait MysqlDatabaseTestCase extends DockerKit {
//  private val client: DockerClient = DefaultDockerClient.fromEnv().build()
//  override implicit val dockerFactory: DockerFactory = new SpotifyDockerFactory(client)
//}

//import java.sql.DriverManager

//import scala.concurrent.ExecutionContext
//import scala.util.Try

//trait DockerPostgresService extends DockerKit {
//  import scala.concurrent.duration._
//  def PostgresAdvertisedPort = 5432
//  def PostgresExposedPort = 44444
//  val PostgresUser = "nph"
//  val PostgresPassword = "suitup"

//  val postgresContainer = DockerContainer("postgres:9.5.3")
//    .withPorts((PostgresAdvertisedPort, Some(PostgresExposedPort)))
//    .withEnv(s"POSTGRES_USER=$PostgresUser", s"POSTGRES_PASSWORD=$PostgresPassword")
////.withReadyChecker(DockerReadyChecker.LogLineContains("waiting for connections on port"))
//    .withReadyChecker(
//      new PostgresReadyChecker(PostgresUser, PostgresPassword, Some(PostgresExposedPort))
//        .looped(15, 1.second)
//    )

//  abstract override def dockerContainers: List[DockerContainer] =
//    postgresContainer :: super.dockerContainers
//}

//class PostgresReadyChecker(user: String, password: String, port: Option[Int] = None)
//    extends DockerReadyChecker {

//  override def apply(container: DockerContainerState)(implicit docker: DockerCommandExecutor,
//                                                      ec: ExecutionContext) =
//    container
//      .getPorts()
//      .map(ports =>
//        Try {
//          Class.forName("org.postgresql.Driver")
//          val url = s"jdbc:postgresql://${docker.host}:${port.getOrElse(ports.values.head)}/"
//          Option(DriverManager.getConnection(url, user, password)).map(_.close).isDefined
//        }.getOrElse(false))
//}
    /**
trait DockerMysqlService extends DockerKit {

  def MysqlAdvertisedPort = 3306
  val MysqlUser = "root"
  val MysqlPassword = "mysecretpassword"
  val MysqlDatabase = "db"

  val mysqlContainer = DockerContainer("mysql:5.7.14")
    .withPorts((MysqlAdvertisedPort, None))
    .withEnv(s"MYSQL_USER=$MysqlUser", s"MYSQL_ROOT_PASSWORD=$MysqlPassword", s"MYSQL_DATABASE=$MysqlDatabase")
    .withReadyChecker(
      DockerReadyChecker
        .LogLineContains("MySQL init process done. Ready for start up.")
    )

  abstract override def dockerContainers: List[DockerContainer] =
    mysqlContainer :: super.dockerContainers
}
     */
