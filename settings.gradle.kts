rootProject.name = "tinsel"

pluginManagement {
    includeBuild("gradle-plugin")
}

include(
    "api",
    "resources",
    "example:plugin",
    "example:pack",
)
