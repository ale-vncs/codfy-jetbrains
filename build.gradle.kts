import org.gradle.configurationcache.extensions.capitalized
import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.0"
  id("org.jetbrains.intellij") version "1.15.0"
  id("com.github.gmazzo.buildconfig") version "4.1.2"
  id("org.jetbrains.changelog") version "2.2.0"
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.imgscalr:imgscalr-lib:4.2")
  implementation("org.kordamp.ikonli:ikonli-swing:12.3.1")
  implementation("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.3.1")
  implementation("se.michaelthelin.spotify:spotify-web-api-java:8.1.0") {
    exclude(group = "org.slf4j", module = "slf4j-api")
  }

  compileOnly("org.slf4j:slf4j-api:1.7.36")
}

// Set the JVM language level used to build the project. Use Java 11 for 2020.3+, and Java 17 for 2022.2+.
kotlin {
  jvmToolchain(17)
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  pluginName = properties("pluginName")
  version = properties("platformVersion")
  type = properties("platformType")

  // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
  plugins = properties("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) }
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
  groups.empty()
  repositoryUrl = properties("pluginRepositoryUrl")
}

buildConfig {
  className("Secret")
  packageName("${project.group}.${project.name}.utils")

  val clientIdKey = "SPOTIFY_CLIENT_ID"
  val clientSecretKey = "SPOTIFY_CLIENT_SECRET"

  buildConfigField("String", clientIdKey, "\"${System.getenv(clientIdKey)}\"")
  buildConfigField("String", clientSecretKey, "\"${System.getenv(clientSecretKey)}\"")
  buildConfigField("String", "APP_NAME", "\"${project.name.capitalized()}\"")
}

tasks {
  wrapper {
    gradleVersion = properties("gradleVersion").get()
  }

  patchPluginXml {
    version = properties("pluginVersion")
    sinceBuild = properties("pluginSinceBuild")
    untilBuild = properties("pluginUntilBuild")

    // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
    pluginDescription = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
      val start = "<!-- Plugin description -->"
      val end = "<!-- Plugin description end -->"

      with (it.lines()) {
        if (!containsAll(listOf(start, end))) {
          throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
        }
        subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
      }
    }

    val changelog = project.changelog // local variable for configuration cache compatibility
    // Get the latest available change notes from the changelog file
    changeNotes = properties("pluginVersion").map { pluginVersion ->
      with(changelog) {
        renderItem(
          (getOrNull(pluginVersion) ?: getUnreleased())
            .withHeader(false)
            .withEmptySections(false),
          Changelog.OutputType.HTML,
        )
      }
    }
  }

  signPlugin {
    certificateChainFile.set(file("certificate/chain.crt"))
    privateKeyFile.set(file("certificate/private.pem"))
    password.set(System.getenv("CERT_PRIVATE_KEY"))
  }

  publishPlugin {
    dependsOn("patchChangelog")
    token.set(System.getenv("PUBLISH_TOKEN"))

    //channels.set(listOf(System.getenv("PUBLISH_CHANNEL") ?: "stable"))
    // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
    // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
    // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
    channels = properties("pluginVersion").map { listOf(it.split('-').getOrElse(1) { "default" }.split('.').first()) }
  }
}
