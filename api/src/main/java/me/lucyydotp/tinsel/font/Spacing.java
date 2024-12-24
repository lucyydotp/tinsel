package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

/**
 * Static methods for interacting with Tinsel's own custom space font.
 */
@NullMarked
public class Spacing {

    private static final Key SPACING_FONT_KEY = Key.key("tinsel", "spacing");

    public static Component spacing(int space) {
        System.out.printf("Creating spacer %s%n", space);
        final var stringBuilder = new StringBuilder();
        var width = Math.abs(space);
        final var isPositive = space > 0;

        for (int i = 10; i >= 0; i--) {
            final var power = 1 << i;
            while (width >= power) {
                width -= power;
                System.out.printf("Adding %s, new width is %s%n", power, width);
                stringBuilder.appendCodePoint(isPositive ? 0xe000 + i : 0xf8ff - i);
            }
        }

        return Component.text(stringBuilder.toString()).font(SPACING_FONT_KEY);
    }
}
