package me.lucyydotp.tinsel.gradle

import org.gradle.api.Project
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.attributes.Usage
import org.gradle.kotlin.dsl.named

internal object DependencyConfigurations {
    const val USAGE = "tinsel-resource-pack"

    internal fun Project.configureDependencyConfig() = configurations.create("resourcePack") {
        it.isCanBeConsumed = true
        it.isCanBeResolved = true
        it.attributes {
            it.attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.LIBRARY))
            it.attribute(Usage.USAGE_ATTRIBUTE, objects.named(USAGE))
            it.attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EMBEDDED))
            it.attribute(LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE, objects.named(USAGE))
        }
    }
}
