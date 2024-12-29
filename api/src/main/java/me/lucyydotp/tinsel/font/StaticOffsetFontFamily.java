package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;

import java.util.Set;

/**
 * A font family that uses the same font for all offsets.
 */
@NullMarked
abstract class StaticOffsetFontFamily implements FontFamily {

    private final Font font;

    protected StaticOffsetFontFamily(Font font) {
        this.font = font;
    }

    @Override
    public final Key key() {
        return font.name();
    }

    @Override
    public final Set<Integer> offsets() {
        return Set.of(0);
    }

    @Override
    public final @NotNull Font offset(int offset) {
        return font;
    }
}
