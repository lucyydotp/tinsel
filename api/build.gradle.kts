repositories {
    mavenCentral()
}

dependencies {
    compileOnlyApi("org.jspecify:jspecify:1.0.0")
    api("net.kyori:adventure-api:4.17.0")
    implementation("com.google.code.gson:gson:2.11.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}

tasks.test {
    useJUnitPlatform()
}
