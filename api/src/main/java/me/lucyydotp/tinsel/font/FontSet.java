package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;

/**
 * A set of {@link FontFamily} objects, available to the client via resource packs.
 */
@NullMarked
public interface FontSet {
    /**
     * The font families that make up this set.
     */
    Map<Key, FontFamily> fonts();

    /**
     * Gets a font family by ID, if it exists.
     */
    default @Nullable FontFamily fontFamily(Key family) {
        return fonts().get(family);
    }

    /**
     * Gets a font by ID, if it exists.
     */
    @Nullable Font font(Key font);
}
