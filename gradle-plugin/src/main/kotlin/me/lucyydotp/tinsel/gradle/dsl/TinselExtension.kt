package me.lucyydotp.tinsel.gradle.dsl

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.provider.Property

/**
 * Configures Tinsel resource generation.
 */
public interface TinselExtension {
    public val fonts: NamedDomainObjectContainer<FontFamily>
    public val packArchiveName: Property<String>
}
