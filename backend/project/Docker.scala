import sbt.Keys.{organization, name, version}
import sbt.{Def, _}
import sbtassembly.AssemblyPlugin.autoImport.assembly
import sbtdocker.{DockerPlugin, ImageName}
import sbtdocker.DockerPlugin.autoImport.{Dockerfile, docker, dockerfile, imageNames}

object Docker extends AutoPlugin {

  object autoImport {
    lazy val exposedPorts = SettingKey[Seq[Int]]("exposed-ports", "A list of awesome operating systems")
  }

  import autoImport._

  override def trigger = allRequirements

  override def requires: Plugins = DockerPlugin

  override def projectSettings: Seq[Def.Setting[_]] =
    Vector(
      exposedPorts := Seq(8666),
      dockerfile in docker := {
        // The assembly task generates a fat JAR file
        val artifact: File = assembly.value
        val artifactTargetPath = s"/app/${artifact.name}"

        new Dockerfile {
          from("frolvlad/alpine-oraclejdk8:slim")
          add(artifact, artifactTargetPath)
          entryPoint("java", "-jar", artifactTargetPath, "-cluster","-cluster-port", "15701")
          expose(exposedPorts.value:_*)
        }
      },
      imageNames in docker := Seq(
        ImageName(s"${organization.value}/${name.value}:latest"),

        ImageName(
          namespace = Some(organization.value),
          repository = name.value,
          tag = Some("v" + version.value)
        )
      )
    )
}
