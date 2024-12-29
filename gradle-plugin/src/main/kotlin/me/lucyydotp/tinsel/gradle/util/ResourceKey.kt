package me.lucyydotp.tinsel.gradle.util

import java.io.Serializable

public data class ResourceKey(
    val namespace: String,
    val name: String,
) : Serializable

public fun ResourceKey(key: String): ResourceKey {
    val split = key.split(":")
    require(split.size == 2) { "Invalid resource key $key" }
    return ResourceKey(split[0], split[1])
}
