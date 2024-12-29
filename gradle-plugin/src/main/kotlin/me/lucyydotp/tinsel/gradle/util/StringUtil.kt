package me.lucyydotp.tinsel.gradle.util

/**
 * Camel-cases a string, replacing underscores with capital letters.
 */
public fun String.camelCase(): String = this
    .split("_", "-")
    .joinToString("") { it.replaceFirstChar(Char::uppercase) }
