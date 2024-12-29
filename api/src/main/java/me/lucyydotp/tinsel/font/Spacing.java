package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

/**
 * Tinsel's own custom spacing font, with some static methods for convenience.
 */
@NullMarked
public class Spacing extends StaticOffsetFontFamily {
    private static final Key SPACING_FONT_KEY = Key.key("tinsel", "spacing");
    private static final int HIGHEST_POWER = 10;
    private static final int POSITIVE_START = 0xE000;
    private static final int NEGATIVE_START = 0xF8FF;

    private static final Spacing instance = new Spacing();

    public static FontFamily font() {
        return instance;
    }

    public static Component spacing(int space) {
        final var stringBuilder = new StringBuilder();
        var width = Math.abs(space);
        final var isPositive = space > 0;

        for (int i = HIGHEST_POWER; i >= 0; i--) {
            final var power = 1 << i;
            while (width >= power) {
                width -= power;
                stringBuilder.appendCodePoint(isPositive ? POSITIVE_START + i : NEGATIVE_START - i);
            }
        }

        return Component.text(stringBuilder.toString()).font(SPACING_FONT_KEY);
    }

    private Spacing() {
        super(new FontImpl(
                SPACING_FONT_KEY,
                SPACING_FONT_KEY,
                0
        ));
    }

    @Override
    public int measureCharacter(int character) {
        if (character >= POSITIVE_START && POSITIVE_START + HIGHEST_POWER >= character) {
           return 1 << (character - POSITIVE_START);
        }
        if (NEGATIVE_START - HIGHEST_POWER <= character && character <= NEGATIVE_START) {
            return -(1 << (NEGATIVE_START - character));
        }
        return FontFamily.PLACEHOLDER_CHARACTER_WIDTH;
    }
}
