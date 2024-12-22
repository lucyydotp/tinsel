plugins {
    id("java")
}

group = "me.lucyydotp"
version = "1.0-SNAPSHOT"

subprojects {
    apply<JavaLibraryPlugin>()
    java.toolchain.languageVersion = JavaLanguageVersion.of(17)
}
