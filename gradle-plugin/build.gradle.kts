plugins {
    kotlin("jvm") version "2.0.20"
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.3.0"
    signing
}

group = "me.lucyydotp.tinsel"
version = "0.2.1"

kotlin {
    jvmToolchain(17)
    explicitApi()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
    implementation(gradleKotlinDsl())
}

signing {
    useGpgCmd()
    sign(configurations.runtimeElements.get())
}

gradlePlugin {
    website = "https://github.com/lucyydotp/tinsel"
    vcsUrl = "https://github.com/lucyydotp/tinsel"
    plugins {
        create("tinsel") {
            id = "me.lucyydotp.tinsel"
            displayName = "Tinsel"
            description = "Assembles Minecraft resource packs for use with the Tinsel server library"
            tags = setOf("minecraft", "resourcepack")
            implementationClass = "me.lucyydotp.tinsel.gradle.TinselGradlePlugin"
        }
    }
}
