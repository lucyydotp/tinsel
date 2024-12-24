package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Map;

/**
 * A set of {@link FontFamily} objects, available to the client via resource packs.
 * The set is guaranteed to contain the font with key {@link FontFamily#DEFAULT_FONT_KEY}.
 */
@NullMarked
public interface FontSet {
    /**
     * The font families that make up this set.
     */
    Map<Key, FontFamily> fontFamilies();

    /**
     * Gets a font family by ID, if it exists.
     */
    default @Nullable FontFamily fontFamily(Key family) {
        return fontFamilies().get(family);
    }

    /**
     * Gets a font by ID, if it exists.
     */
    @Nullable Font font(Key font);

    /**
     * Gets the default vanilla font with any configured offsets.
     */
    FontFamily defaultFont();

    /**
     * Creates a font set from a series of families.
     * @param families the families to add to the set
     */
    @Contract("_ -> new")
    static FontSet of(FontFamily... families) {
        return FontSetImpl.ofFamilies(families);
    }
}
