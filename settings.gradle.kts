rootProject.name = "tinsel"

pluginManagement {
    includeBuild("gradle-plugin")
}

include(
    "api",
    "resources",
    "test-plugin",
)
