import sbt._
import Keys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._

object Build extends sbt.Build {
  lazy val root = project.in(file(".")).settings(
    scalaJSSettings:_*
  ).settings(
    name := "gridworld",
    scalaVersion := "2.10.4",
    libraryDependencies += "org.scala-lang.modules.scalajs" %% "scalajs-dom" % "0.3",
    libraryDependencies += "org.scala-lang.modules.scalajs" %% "scalajs-jquery" % "0.3"  )
}