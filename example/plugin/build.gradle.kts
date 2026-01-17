import org.gradle.kotlin.dsl.named

plugins {
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "9.0.0-beta4"
    java
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

val resourcePack by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
    attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named("tinsel-resource-pack"))
}

val shade by configurations.creating

configurations.implementation.configure {
    extendsFrom(shade)
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    shade(project(":api"))
    resourcePack(project(":example:pack"))
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
}

tasks {
    shadowJar {
        configurations = setOf(shade)
    }

    runServer {
        dependsOn(resourcePack)
        minecraftVersion("1.21.11")
        environment(
            "TINSEL_PACK_PATH",
            resourcePack.singleFile.absolutePath
        )
    }
}
