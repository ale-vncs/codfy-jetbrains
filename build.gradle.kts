import org.gradle.configurationcache.extensions.capitalized

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.0"
  id("org.jetbrains.intellij") version "1.15.0"
  id("com.github.gmazzo.buildconfig") version "4.1.2"
}

group = "com.ale.vncs"
version = "0.1.2"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.imgscalr:imgscalr-lib:4.2")
  implementation("org.kordamp.ikonli:ikonli-swing:12.3.1")
  implementation("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.3.1")
  implementation("se.michaelthelin.spotify:spotify-web-api-java:8.0.0") {
    exclude(group = "org.slf4j", module = "slf4j-api")
  }

  compileOnly("org.slf4j:slf4j-api:1.7.36")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  version.set("2022.2.5") //2022.2.5 - 223.8836.41
  type.set("IC") // Target IDE Platform

  plugins.set(listOf(/* Plugin Dependencies */))
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
  // Set the JVM compatibility versions
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
  }

  patchPluginXml {
    sinceBuild.set("222")
    untilBuild.set("232.*")
  }

  signPlugin {
    certificateChainFile.set(file("certificate/chain.crt"))
    privateKeyFile.set(file("certificate/private.pem"))
    password.set(System.getenv("CERT_PRIVATE_KEY"))
  }

  publishPlugin {
    channels.set(listOf(System.getenv("PUBLISH_CHANNEL") ?: "stable"))
    token.set(System.getenv("PUBLISH_TOKEN"))
  }
}
