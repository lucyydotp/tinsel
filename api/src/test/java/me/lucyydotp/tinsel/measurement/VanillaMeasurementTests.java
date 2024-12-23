package me.lucyydotp.tinsel.measurement;

import me.lucyydotp.tinsel.font.FontFamily;
import me.lucyydotp.tinsel.font.FontSet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests the text width measurer against some strings with the vanilla font.
 */
public class VanillaMeasurementTests {
    private static final String TEST_STRING = "The Quick Brown Fox Jumped Over The Lazy Dog.";

    private final TextWidthMeasurer measurer = new TextWidthMeasurer(
            FontSet.of(FontFamily.vanilla())
    );

    @Test
    public void measurePlain() {
        Assertions.assertEquals(
                245,
                measurer.measure(Component.text(TEST_STRING))
        );
    }

    @Test
    public void measureBold() {
        Assertions.assertEquals(
                290,
                measurer.measure(Component.text(TEST_STRING).decorate(TextDecoration.BOLD))
        );
    }

    @Test
    public void testUnicodeSurrogates() {
        Assertions.assertEquals(
                93,
                measurer.measure(Component.text("Ꞩꞩ\uD800\uDF30\uD800\uDF31\uD800\uDF32\uD800\uDF33\uD800\uDF34\uD800\uDF35\uD800\uDF36\uD800\uDF37\uD800\uDF38\uD800\uDF39\uD800\uDF3A\uD800\uDF3B\uD800\uDF3C\uD800\uDF3D"))
        );
    }
}
