rootProject.name = "tinsel"

include("api")

rootProject.children.forEach { it.name = "${rootProject.name}-${it.name}" }
