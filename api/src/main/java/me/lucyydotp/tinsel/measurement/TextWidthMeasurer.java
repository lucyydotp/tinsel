package me.lucyydotp.tinsel.measurement;

import me.lucyydotp.tinsel.font.FontFamily;
import me.lucyydotp.tinsel.font.FontSet;
import me.lucyydotp.tinsel.util.Utf8Util;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Measures the width of text components.
 */
@NullMarked
public class TextWidthMeasurer {
    private static final Logger logger = Logger.getLogger(TextWidthMeasurer.class.getSimpleName());

    private final FontSet fonts;

    public TextWidthMeasurer(FontSet fonts) {
        this.fonts = fonts;
    }

    /**
     * Finds a font family with a given key.
     *
     * @param fontKey the font family's key
     * @return Null when `fontKey` is null. Otherwise, the font family, or the default font if no font with the given key exists.
     */
    @Contract("null -> null; !null -> !null")
    private @Nullable FontFamily getFontFamily(@Nullable Key fontKey) {
        if (fontKey == null) {
            return null;
        }

        final var font = fonts.fontFamily(fontKey);
        if (font == null) {
            logger.warning("Unknown font key " + fontKey + "! Using the default font.");
            return FontFamily.vanilla();
        }
        return font;
    }

    /**
     * Measures a string with a font family.
     *
     * @param string the string to measure
     * @param bold   if the string is bold, which will add an extra pixel to each glyph
     * @param family the font family the string is displayed with
     * @return the string's pixel width
     */
    private int measureString(String string, boolean bold, FontFamily family) {
        var length = 0;
        for (final var chr : Utf8Util.codePoints(string)) {
            length += family.measureCharacter(chr);
            if (bold) length++;

        }
        return length;
    }

    /**
     * Measures a component.
     *
     * @param text         the component to measure
     * @param parentIsBold whether the component inherits bold styling from its parent
     * @param activeFont   the font family inherited from the parent component, or the default font
     * @return the width of the component's text and children
     */
    private int measureComponent(Component text, boolean parentIsBold, FontFamily activeFont) {
        final var font = Objects.requireNonNullElse(getFontFamily(text.font()), activeFont);
        var count = 0;

        final var bold = switch (text.style().decoration(TextDecoration.BOLD)) {
            case TRUE -> true;
            case FALSE -> false;
            case NOT_SET -> parentIsBold;
        };

        if (text instanceof TextComponent textComponent) {
            count += measureString(
                    textComponent.content(),
                    bold,
                    font
            );
        }
        // TODO(lucy): measure more types (translatable, score, nbt, etc.)

        for (final var child : text.children()) {
            count += measureComponent(child, bold, font);
        }

        return count;
    }

    /**
     * Measures the width of a component, in scaled GUI pixels.
     *
     * @param text the component to measure
     * @return the component's displayed width
     */
    public int measure(Component text) {
        // TODO(lucy): the last pixel overshoots the last glyph by 1, presumably for letter spacing. confirm this is correct
        return measureComponent(text, false, FontFamily.vanilla());
    }
}
