package me.lucyydotp.tinsel.gradle.dsl;

import me.lucyydotp.tinsel.gradle.util.ResourceKey
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import java.io.Serializable
import javax.inject.Inject

public abstract class FontFamily @Inject constructor(public val name: String) : Serializable {

    public abstract val offsets: SetProperty<Int>
    public abstract val addedBitmapSpacing: Property<Int>
    public abstract val outputNamespace: Property<String>

    public val key: ResourceKey = ResourceKey(name)

    init {
        outputNamespace.convention(key.namespace)
    }
}
