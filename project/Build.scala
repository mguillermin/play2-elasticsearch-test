import java.io.File
import sbt._
import Keys._
import PlayProject._
import scalax.file.PathFinder

object ApplicationBuild extends Build {

    val appName         = "play2-elasticsearch"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "org.elasticsearch" % "elasticsearch" % "0.19.2",
      "org.twitter4j" % "twitter4j-core" % "[2.2,)"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here
      lessEntryPoints <<= (sourceDirectory in Compile)(base => (
          (base / "assets" / "stylesheets" / "bootstrap" * "bootstrap.less") +++
          (base / "assets" / "stylesheets" / "main" * "*.less")
      )),
      resolvers += "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
    )

}
