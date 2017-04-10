import sbtassembly.AssemblyPlugin.autoImport._
import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import UnidocKeys._
import com.typesafe.sbt.pgp.PgpKeys._


lazy val commonScalacOptions = Seq(
  "-feature",
  "-deprecation",
  "-encoding", "utf8",
  "-language:postfixOps",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xcheckinit",
  "-Xfuture",
  "-Xlint",
  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-value-discard",
  "-Yno-imports",
  "-Yno-predef")

lazy val buildSettings = Seq(
  name := "data",
  organization in Global := "com.waioeka",
  scalaVersion in Global := "2.12.1"
)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false,
  publishSigned := ()
)

lazy val credentialSettings = Seq(
  credentials ++= (for {
    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := Function.const(false),
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("Snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("Releases" at nexus + "service/local/staging/deploy/maven2")
  },
  homepage := Some(url("https://github.com/lewismj/data")),
  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),
  scmInfo := Some(ScmInfo(url("https://github.com/lewismj/data"), "scm:git:git@github.com:lewismj/data.git")),
  autoAPIMappings := true,
  pomExtra := (
    <developers>
      <developer>
        <name>Michael Lewis</name>
        <url>https://www.waioeka.com</url>
      </developer>
    </developers>
  )
) ++ credentialSettings

lazy val scoverageSettings = Seq(
  coverageMinimum := 75,
  coverageFailOnMinimum := true
)

lazy val commonSettings = Seq(
  scalacOptions ++= commonScalacOptions,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats" % "0.9.0",
    "com.chuusai" %% "shapeless" % "2.3.2"
  ),
  fork in test := true
)

lazy val dataSettings = buildSettings ++ commonSettings ++ scoverageSettings

lazy val data = project.in(file("."))
  .settings(moduleName := "root")
  .settings(noPublishSettings:_*)
  .aggregate(tests, core)

lazy val core = project.in(file("core"))
  .settings(moduleName := "data-core")
  .settings(dataSettings:_*)
  .settings(publishSettings:_*)

lazy val tests = project.in(file("tests"))
  .dependsOn(core)
  .settings(moduleName := "data-tests")
  .settings(dataSettings:_*)
  .settings(noPublishSettings:_*)
  .settings(
    coverageEnabled := false,
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-laws" % "0.9.0",
      "org.scalatest"  %% "scalatest" % "3.0.0" % "test",
      "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
    )
  )

