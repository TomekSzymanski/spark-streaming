name := "spark-streaming"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.12"

val sparkVersion="2.4.5"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-encoding", "utf8",
  "-unchecked",
  "-Xlint",
  "-Xfatal-warnings",
  "-Xfuture",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-value-discard",
  "-Ywarn-adapted-args"
)

wartremoverErrors ++= Seq(Wart.Product, Wart.Return, Wart.TryPartial, Wart.Enumeration, Wart.JavaConversions, Wart.LeakingSealed, Wart.Serializable, Wart.Var, Wart.While, Wart.EitherProjectionPartial)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-avro" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,
  "com.typesafe" % "config" % "1.4.0",
  "org.scalactic" %% "scalactic" % "3.0.0" % "test,it",
  "org.scalatest" %% "scalatest" % "3.0.0" % "test,it",
  "org.mockito" % "mockito-core" % "1.10.19" % "test,it",
  "com.holdenkarau" %% "spark-testing-base" % s"${sparkVersion}_0.14.0" % "test,it"
)

// required for testing with Spark context, recommended by spark-testing-base
fork in IntegrationTest := true
javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled")
// required by spark-testing-base
parallelExecution in IntegrationTest := false

Defaults.itSettings

assemblyMergeStrategy in assembly := {
  case PathList("org", "apache", "spark", "unused", "UnusedStubClass.class") => MergeStrategy.first
  case "application.conf" => MergeStrategy.concat
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

lazy val `spark-streaming` = project.in(file(".")).configs(IntegrationTest)
