plugins {
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "9.0.0-beta4"
    java
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

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
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
}

tasks {
    shadowJar {
        configurations = setOf(shade)
    }

    runServer {
        minecraftVersion("1.21.4")
    }
}
