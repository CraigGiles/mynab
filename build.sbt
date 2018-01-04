val commonSettings = Seq(
  organization := "com.gilesc",
  scalaVersion in ThisBuild := "2.12.4",
  version := "0.0.1",
  initialCommands in console := """
    |import cats.implicits._
    |""".stripMargin,
  scalacOptions := Seq(
    // Target the 1.8 JVM since we will be using scala 2.12 as a default for
    // the scalaVersion flag.
    "-target:jvm-1.8",

    // Since not all developers use the same operating systems / default
    // character encodings, we specify the desired encoding so the compiler
    // can warn us about any changes during editing.
    "-encoding", "UTF-8",

    // unchecked will provide detailed information about erasure warnings.
    "-unchecked",

    // When using deprecated values in code, give us more information about
    // what is deprecated and where its use is inside our codebase.
    "-deprecation",

    // Turn on future language features and guard against a few deprecated
    // features around Futures.
    "-Xfuture",

    // Don't allow the scala compiler to convert multiple arguments into a
    // single tuple for use with as parameterized function.
    "-Yno-adapted-args",

    // Warn us when code that is unreachable is detected.
    "-Ywarn-dead-code",

    // Alert us when there is an implicit numeric widening.
    "-Ywarn-numeric-widen",

    // Warn us when we're returning a value after specifying the return is Unit.
    "-Ywarn-value-discard",

    // Warn us when a function or value is defined but never used.
    "-Ywarn-unused",

    // Kill the compile on warnings
    // "-Xfatal-warnings",

    // Provide more information about often misused language features
    // such as higher kinded types or implicitConversions.
    "-feature",

    // Enable language features. If you want to import all of the following
    // features, simply use -language:_, otherwise specify each feature you
    // with to import specifically by using the following: -language:feature
    // - implicitConversions: Conversions from A -> B implicitly
    // - existentials: Example - Array[T] forSome { type T }
    // - higherKinds: Generic abstractions, IE: M[_] is a box M
    // - experimental.macros: Macros.. need I say more
    // - reflectiveCalls: Enable Reflection in scala
    // - dynamics: Add dynamic types for more advanced DSLs
    // - postfixOps: Call nullary methods without dot operator.
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:postfixOps",

    // the strategy used for translating lambdas into JVM code.
    // The current standard is "inline" but they're moving towards "method."
    "-Ydelambdafy:method"
  ),
  scalacOptions in (Compile, console) ~= (_.filterNot(_ == "-Xlint")),
  scalacOptions in (Compile, console) ~= (_.filterNot(_ == "-Ywarn-unused-import"))
)

addCommandAlias("migrate", ";flyway/flywayMigrate")

lazy val rootProject = (project in file("."))
  .settings(commonSettings)
  .settings(
    name := "mynab",
    aggregate in update := false)
  .aggregate(arrow, domain, service, mysql, `akka-http`)

lazy val arrow = (project in file("arrow"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Dependencies.arrow)

lazy val flyway = (project in file("flyway"))
  .settings(commonSettings)
  .enablePlugins(FlywayPlugin)
  .settings(libraryDependencies ++= Dependencies.flyway)

lazy val domain = Project("domain", file("domain"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Dependencies.domain)

lazy val mysql = Project("mysql", file("mysql"))
  .settings(commonSettings)
  .dependsOn(
    domain % "test->test;test->compile;compile->compile",
    testkit % "test->test;test->compile;compile->compile")
  .settings(libraryDependencies ++= Dependencies.mysql)

lazy val service = Project("service", file("service"))
  .settings(commonSettings)
  .dependsOn(
    testkit % "test->test;test->compile;compile->compile",
    domain % "test->test;test->compile;compile->compile",
    arrow % "test->test;test->compile;compile->compile",
    mysql % "test->test;test->compile;compile->compile")
  .settings(libraryDependencies ++= Dependencies.service)

lazy val `akka-http` = Project("akka-http", file("akka-http"))
  .enablePlugins(SbtWeb)
  .settings(commonSettings)
  .dependsOn(
    service % "test->test;test->compile;compile->compile",
    testkit % "test->test;test->compile;compile->compile")
  .settings(libraryDependencies ++= Dependencies.akkahttp)
  .settings(
    pipelineStages in Assets := Seq(scalaJSPipeline),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    WebKeys.packagePrefix in Assets := "public/",
    managedClasspath in Runtime += (packageBin in Assets).value
  )

lazy val scalajs = Project("scalajs", file("scalajs"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSWeb)
  .settings(libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.3",
        "com.lihaoyi" %%% "scalatags" % "0.6.7"))
  .settings(scalaJSUseMainModuleInitializer := true)

lazy val testkit = Project("testkit", file("testkit"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Dependencies.testkit)
  .dependsOn(domain % "test->test;test->compile;compile->compile")
