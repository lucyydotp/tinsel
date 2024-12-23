package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
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
     * The Minecraft default font's key.
     */
    Key DEFAULT_FONT_KEY = Key.key("minecraft", "default");

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
     * <p>
     * Characters are encoded as ints and not chars because Java chars cannot encode surrogate pairs as a single value.
     *
     * @return the character's width
     */
    int measureCharacter(int character);


    /**
     * The vanilla font family with offset 0, built into the game's default assets.
     */
    static FontFamily vanilla() {
        return FontFamilyImpl.VANILLA_FONT;
    }

    /**
     * Creates a font family using the vanilla definition with offsets.
     * @param offsets the offset fonts to use
     */
    @Contract(pure = true, value = "_ -> new")
    static FontFamily vanillaWithOffsets(Map<Integer, Key> offsets) {
        return FontFactory.vanillaWithOffsets(offsets);
    }

    /**
     * Loads a font family from a pre-generated font family definition file. Note that this is a Tinsel-format file,
     * not a vanilla font file.
     *
     * @param path the path to the definition file
     * @throws IOException if the file cannot be loaded
     */
    @Contract(pure = true, value = "_ -> new")
    static FontFamily fromFile(Path path) throws IOException {
        return FontFactory.fromJson(Files.newInputStream(path));
    }
}
