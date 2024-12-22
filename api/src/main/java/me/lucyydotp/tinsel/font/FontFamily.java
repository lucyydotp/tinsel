package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Set;

/**
 * A collection of resource-pack fonts with the same content but different height offsets.
 * <p>
 * Height offsets are separate fonts in the resource pack with identical glyphs, but adjusted ascent values such that
 * they are vertically offset from each other. The base font is considered to have an offset of 0, with positive offsets
 * moving the font down, and negative offsets moving the font up. Offsets are defined in scaled GUI pixels.
 */
@NullMarked
public interface FontFamily {
    /**
     * The width of the game's placeholder glyph that gets rendered when a glyph is not defined.
     */
    int PLACEHOLDER_CHARACTER_WIDTH = 5;

    /**
     * The font's unique identifier. The resource pack is expected to provide this key as a font with offset 0.
     */
    Key key();

    /**
     * The available height offsets for this font family.
     */
    Set<Integer> offsets();

    /**
     * Gets the font with a given height offset.
     *
     * @param offset the offset
     * @return the font with this offset, or null if it doesn't exist
     */
    @Nullable
    Font offset(int offset);

    /**
     * Gets the width that a character will render at, not accounting for letter spacing.
     * If the font does not define the character, the width of its placeholder should be provided. which is
     * defined by {@link #PLACEHOLDER_CHARACTER_WIDTH}.
     *
     * @return the character's width
     */
    int measure(char character);


    /**
     * The vanilla font family with offset 0, built into the game's default assets.
     */
    static FontFamily vanilla() {
        // TODO(lucy)
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
