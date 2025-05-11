package me.lucyydotp.tinsel.gradle

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.kotlin.dsl.named

internal object DependencyConfigurations {
    const val USAGE = "tinsel-resource-pack"

    private fun Project.setAttributes(configuration: Configuration) = configuration.attributes {
        it.attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
        it.attribute(Usage.USAGE_ATTRIBUTE, objects.named(USAGE))
        it.attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EMBEDDED))
        it.attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(USAGE))
    }

    internal fun Project.createIncludeConfiguration() = configurations.register(TinselGradlePlugin.INCLUDE_CONFIGURATION) {
        it.isCanBeConsumed = false
        it.isCanBeResolved = true
        setAttributes(it)
    }

    internal fun Project.createResourcePackConfiguration() = configurations.register(TinselGradlePlugin.RESOURCE_PACK_CONFIGURATION) {
        it.isTransitive = false
        it.isCanBeConsumed = true
        it.isCanBeResolved = false
        it.isCanBeDeclared = false
        setAttributes(it)
    }
}
