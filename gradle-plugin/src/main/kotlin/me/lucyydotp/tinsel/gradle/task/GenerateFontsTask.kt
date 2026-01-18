package me.lucyydotp.tinsel.gradle.task

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import me.lucyydotp.tinsel.gradle.TinselGradlePlugin
import me.lucyydotp.tinsel.gradle.addSpaceToBitmapFont
import me.lucyydotp.tinsel.gradle.util.ResourceKey
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import javax.imageio.ImageIO

public abstract class GenerateFontsTask : DefaultTask() {
    private companion object {
        // pretty-print for development, will be minified for prod
        private val gson = GsonBuilder().setPrettyPrinting().create()
    }

    @get:Input
    public abstract val key: Property<ResourceKey>

    @get:Input
    public abstract val offsets: SetProperty<Int>

    @get:Input
    public abstract val outputNamespace: Property<String>

    @get:Input
    @get:Optional
    public abstract val bitmapSpacing: Property<Int>

    @get:InputFiles
    public val inputFiles: FileCollection
        get() = project.files(
            "src/main/fontBitmaps",
            definitionFile,
        )

    @get:OutputDirectory
    public abstract val outputDirectory: DirectoryProperty

    init {
        outputNamespace.convention(key.map(ResourceKey::namespace))
        outputDirectory.convention(outputNamespace.map { ns ->
            project.layout.projectDirectory.dir("build/tinsel/assets/${ns}")
        })
    }

    private val definitionFile = key.map { (ns, id) ->
        if (ns == TinselGradlePlugin.MINECRAFT_NAMESPACE) {
            project.file("src/main/vanilla/$id.json") // TODO
        } else {
            project.file("src/main/resources/assets/$ns/font/$id.json")
        }
    }

    private fun createOffset(definitionFile: JsonObject, offset: Int) = definitionFile.deepCopy().apply {
        getAsJsonArray("providers").forEach {
            val provider = it.asJsonObject
            when (provider["type"].asString) {
                "bitmap" -> {
                    val height = provider["height"]?.takeIf(JsonElement::isJsonPrimitive)?.asInt ?: 8
                    val currentAscent = provider["ascent"].asInt
                    val newAscent = currentAscent - offset
                    check(newAscent <= height) { "Ascent $newAscent (${if (offset > 0) "+" else ""}${offset}) is higher than font height $height" }

                    provider.addProperty(
                        "ascent",
                        newAscent
                    )
                }
                // TODO(lucy): ttf
            }
        }
    }

    @TaskAction
    public fun action() {
        outputDirectory.get().asFile.deleteRecursively()
        val def = gson.fromJson(definitionFile.get().readText(), JsonObject::class.java)
        def.getAsJsonArray("providers").forEach {
            val provider = it.asJsonObject
            if (bitmapSpacing.isPresent && provider["type"].asString == "bitmap") {
                val key = ResourceKey(provider["file"].asString)
                val textureName = "${key.name.removeSuffix(".png")}_offset_${bitmapSpacing.get()}.png"
                val texturePath = outputDirectory.get().file("textures/${textureName}").asFile

                texturePath.parentFile.mkdirs()
                texturePath.outputStream().use {
                    ImageIO.write(
                        addSpaceToBitmapFont(
                            project.file("src/main/fontBitmaps/${key.namespace}/${key.name}").inputStream(),
                            provider["chars"].asJsonArray.size(),
                            bitmapSpacing.get()
                        ),
                        "png",
                        it
                    )
                }

//                provider.addProperty("ascent",  provider["ascent"].asInt + bitmapSpacing.get())
                provider.addProperty("height", (provider["height"]?.asInt ?: 8) + bitmapSpacing.get())
                provider.addProperty("file", "${outputNamespace.get()}:$textureName")
            }
        }

        offsets.get().forEach { offset ->
            outputDirectory.get()
                .file(if (offset == 0) "font/${key.get().name}.json" else "font/${key.get().name}_offset_$offset.json")
                .asFile
                .also { it.parentFile.mkdirs() }
                .writeText(gson.toJson(createOffset(def, offset)))

        }
    }
}
