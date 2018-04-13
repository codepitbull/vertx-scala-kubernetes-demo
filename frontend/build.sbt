import sbt.Package._
import sbt._
import Docker.autoImport.exposedPorts

scalaVersion := "2.12.4"

version := "5"
organization := "codepitbull"

enablePlugins(DockerPlugin)
exposedPorts := Seq(8667)

libraryDependencies ++= Vector (
  Library.vertx_lang_scala,
  Library.vertx_web,
  Library.vertx_bridge_common,
  Library.scalaTest       % "test",
  Library.vertx_hazelcast,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.hazelcast" % "hazelcast-kubernetes" % "1.1.0",

  //required to get rid of some warnings emitted by the scala-compile
  Library.vertx_codegen
)

packageOptions += ManifestAttributes(
  ("Main-Verticle", "scala:de.codepitbull.vertx.scala.kubernetes.frontend.ChatVerticle"))

