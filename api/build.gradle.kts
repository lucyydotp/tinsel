plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.gradleup.nmcp") version "0.0.8"
}

java.toolchain.languageVersion = JavaLanguageVersion.of(17)

repositories {
    mavenCentral()
}

dependencies {
    compileOnlyApi("org.jspecify:jspecify:1.0.0")
    api("net.kyori:adventure-api:4.17.0")
    implementation("com.google.code.gson:gson:2.11.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}

base {
    archivesName = "tinsel-api"
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        pom {
            name = "Tinsel"
            artifactId = "tinsel-api"
            description = "A library for Minecraft Java servers to do fun things with text."
            url = "https://github.com/lucyydotp/tinsel.git"

            scm {
                url = "https://github.com/lucyydotp/tinsel"
                connection = "scm:git:ssh://github.com/lucyydotp/tinsel.git"
                developerConnection = "scm:git:ssh://github.com/lucyydotp/tinsel.git"
            }

            licenses {
                license {
                    name = "MIT"
                    url = "https://opensource.org/license/mit"
                }
            }

            developers {
                developer {
                    id = "lucyydotp"
                    name = "Lucy Poulton"
                    email = "lucy@lucyydotp.me"
                }
            }
        }
    }
}

nmcp {
    publish("maven") {
        username = project.ext["sonatypeCentralUsername"] as String
        password = project.ext["sonatypeCentralPassword"] as String
        publicationType = "AUTOMATIC"
    }
}

tasks.publish {
    dependsOn(tasks.publishAllPublicationsToCentralPortal)
}
