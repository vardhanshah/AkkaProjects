name := "actor_model"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.21",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.21",
  "com.typesafe.akka" %% "akka-cluster" % "2.5.21",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "com.ning" % "async-http-client" % "1.9.40" ,
  "org.jsoup" % "jsoup" % "1.8.1",
  "ch.qos.logback" % "logback-classic" % "1.1.4" 
)