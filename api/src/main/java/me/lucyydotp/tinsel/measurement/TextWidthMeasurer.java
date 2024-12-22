package me.lucyydotp.tinsel.measurement;

import me.lucyydotp.tinsel.font.FontFamily;
import me.lucyydotp.tinsel.font.FontSet;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
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

    private int measureString(String string, boolean bold, FontFamily family) {
        var length = 0;
        for (final var chr : string.toCharArray()) {
            length += family.measure(chr);
            if (bold) length++;

        }
        return length;
    }

    private int calculate(Component text, boolean parentIsBold, FontFamily activeFont) {
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

        for (final var child : text.children()) {
            count += calculate(child, bold, font);
        }

        return count;
    }


    public int measure(TextComponent text) {
        return calculate(text, false, FontFamily.vanilla());
    }
}
