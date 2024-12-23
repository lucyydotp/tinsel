package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * A basic {@link Font} implementation.
 */
@NullMarked
record FontImpl(
        Key name,
        Key family,
        int offset
) implements Font {
}
