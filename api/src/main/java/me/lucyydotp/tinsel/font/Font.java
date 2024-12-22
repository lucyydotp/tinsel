package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.StyleBuilderApplicable;
import org.jspecify.annotations.NullMarked;

/**
 * A font as part of a {@link FontFamily}, potentially with a vertical offset.
 */
@NullMarked
public interface Font extends StyleBuilderApplicable {
    /**
     * The font's name as defined in the resource pack.
     */
    Key name();

    /**
     * The font's family.
     * @see FontFamily
     */
    Key family();

    /**
     * The font's vertical offset.
     */
    int offset();

    /**
     * Applies the font to a style.
     */
    @Override
    default void styleApply(Style.Builder style) {
        style.font(name());
    }
}
