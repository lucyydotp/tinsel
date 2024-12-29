package me.lucyydotp.tinsel.gradle

import me.lucyydotp.tinsel.gradle.DependencyConfigurations.configureDependencyConfig
import me.lucyydotp.tinsel.gradle.dsl.TinselExtension
import me.lucyydotp.tinsel.gradle.task.GenerateFontsTask
import me.lucyydotp.tinsel.gradle.util.camelCase
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register


public class TinselGradlePlugin : Plugin<Project> {
    internal companion object {
        const val MINECRAFT_NAMESPACE = "minecraft"
        const val TASK_GROUP = "tinsel"
    }

    override fun apply(project: Project) {
        val dependencyConfig = project.configureDependencyConfig()

        val ext = project.extensions.create<TinselExtension>("tinsel")
        ext.packArchiveName.convention("resources.zip")

        ext.fonts.all { font ->
            project.tasks.register<GenerateFontsTask>(
                "generate${font.key.namespace.camelCase()}${font.key.name.camelCase()}Font",
            ) {
                group = TASK_GROUP
                key.set(font.key)
                outputNamespace.set(font.outputNamespace)
                offsets.set(font.offsets)
                bitmapSpacing.set(font.addedBitmapSpacing)
            }
        }

        val generateFontsTask = project.tasks.register<Task>("generateFonts") {
            group = TASK_GROUP
            dependsOn(project.tasks.filterIsInstance<GenerateFontsTask>())
            outputs.dir("build/tinsel")
        }

        val assembleTask = project.tasks.register<Zip>("assembleResourcePack") {
            group = TASK_GROUP
            from(dependencyConfig.map { project.zipTree(it) })
            from(generateFontsTask).into("")
            from("src/main/resources")
            duplicatesStrategy = DuplicatesStrategy.INCLUDE

            destinationDirectory.set(project.file("build/distributions"))
            archiveFileName.set(ext.packArchiveName)
        }

        project.tasks.register<Task>("build") {
            dependsOn(assembleTask)
        }

        project.artifacts.add(dependencyConfig.name, assembleTask)

        System.getenv()["MINECRAFT_RESOURCE_PACKS_FOLDER"]?.let { folder ->
            project.tasks.register("copyToMinecraft", Copy::class.java) {
                it.from(assembleTask.map { it.outputs.files })
                it.destinationDir = project.file(folder)
            }
        }
    }
}
