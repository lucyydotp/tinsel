package me.lucyydotp.tinsel.gradle

import me.lucyydotp.tinsel.gradle.dsl.TinselExtension
import me.lucyydotp.tinsel.gradle.task.GenerateFontsTask
import me.lucyydotp.tinsel.gradle.util.camelCase
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Zip


public class TinselGradlePlugin : Plugin<Project> {
    internal companion object {
        const val MINECRAFT_NAMESPACE = "minecraft"
        const val TASK_GROUP = "tinsel"
    }

    override fun apply(project: Project) {
        val ext = project.extensions.create("tinsel", TinselExtension::class.java)
        ext.packArchiveName.convention("resources.zip")

        ext.fonts.all { font ->
            project.tasks.register(
                "generate${font.key.namespace.camelCase()}${font.key.name.camelCase()}Font",
                GenerateFontsTask::class.java
            ) {
                it.group = TASK_GROUP
                it.key.set(font.key)
                it.outputNamespace.set(font.outputNamespace)
                it.offsets.set(font.offsets)
                it.bitmapSpacing.set(font.addedBitmapSpacing)
            }
        }

        val generateFontsTask = project.tasks.register("generateFonts") {
            it.group = TASK_GROUP
            it.dependsOn(it.project.tasks.filterIsInstance<GenerateFontsTask>())
            it.outputs.dir("build/tinsel")
        }

        val assembleTask = project.tasks.register("assembleResourcePack", Zip::class.java) {
            it.group = TASK_GROUP
            it.from(generateFontsTask).into("")
            it.from("src/main/resources")

            it.destinationDirectory.set(project.file("build/distributions"))
            it.archiveFileName.set(ext.packArchiveName)
        }

        project.tasks.register("build") {
            it.dependsOn(assembleTask)
        }

        System.getenv()["MINECRAFT_RESOURCE_PACKS_FOLDER"]?.let { folder ->
            project.tasks.register("copyToMinecraft", Copy::class.java) {
                it.from(assembleTask.map { it.outputs.files })
                it.destinationDir = project.file(folder)
            }
        }
    }
}
