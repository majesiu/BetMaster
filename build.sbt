name := "BetMaster"

version := "1.0"

lazy val `betmaster` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test, "org.scalaj" %% "scalaj-http" % "2.2.1", "org.webjars.bower" % "bootstrap-sass" % "3.3.6")

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  