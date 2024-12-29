import me.lucyydotp.tinsel.gradle.GenerateFontBitmapsTask

val TEMPLATE_FONT = "src/main/templates/offset_font.json.template"
val REPLACE_EXPRESSION = Regex("\\\$\\{offset:(-?\\d+)}")
val OFFSETS = setOf(
    -4, -8, -12, -16, -20, -24, -28, -32, -36, -40, -44, -48,
    4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48
)

val generateFontOffsetsTask = task("generateFontOffsets") {
    inputs.file(TEMPLATE_FONT)
    inputs.property("offsets", OFFSETS)
    outputs.dir("build/templates")

    doFirst {
        val templateText = file(TEMPLATE_FONT).readText()
        OFFSETS.forEach {offset ->
            outputs.files.first().resolve("default_offset_$offset.json").writeText(
                templateText.replace(REPLACE_EXPRESSION) {
                    (it.groups[1]!!.value.toInt() - offset).toString()
                }
            )
        }
    }
}

task<GenerateFontBitmapsTask>("generateSpacedBitmap") {
    "src/main/vanilla/ascii.png" hasLines 16
    "src/main/vanilla/accented.png" hasLines 75
    "src/main/vanilla/nonlatin_european.png" hasLines 67
    addedSpacing = 48
}

val packageTask = task<Zip>("assemble") {
    from(generateFontOffsetsTask) {
        into("assets/tinsel/font")
    }

    from(tasks["generateSpacedBitmap"]) {
        into("assets/tinsel/textures/font")
    }

    from(file("src/main/resources"))
    destinationDirectory = file("build/distributions")
    archiveFileName = "tinsel-resources.zip"
}

System.getenv()["MINECRAFT_RESOURCE_PACKS_FOLDER"]?.let {
    task<Copy>("copyToMinecraft") {
        from(packageTask.outputs.files)
        destinationDir = file(it)
    }
}
