plugins {
    kotlin("jvm") version "2.0.20"
    `java-gradle-plugin`
}

kotlin {
    explicitApi()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
    implementation(gradleKotlinDsl())
}

gradlePlugin {
    plugins {
        create("tinsel") {
            id = "me.lucyydotp.tinsel"
            implementationClass = "me.lucyydotp.tinsel.gradle.TinselGradlePlugin"
        }
    }
}
