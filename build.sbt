name := """FinanceApp"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.yahoofinance-api" % "YahooFinanceAPI" % "3.15.0"
libraryDependencies += "org.mockito" % "mockito-core" % "4.0.0" % Test
libraryDependencies += "org.mockito" %% "mockito-scala-scalatest" % "1.16.46" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
