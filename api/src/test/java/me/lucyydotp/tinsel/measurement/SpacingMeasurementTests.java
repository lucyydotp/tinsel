package me.lucyydotp.tinsel.measurement;

import me.lucyydotp.tinsel.font.FontSet;
import me.lucyydotp.tinsel.font.Spacing;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests font measurement with the spacing font.
 */
public class SpacingMeasurementTests {
    private static final Key SPACING_FONT_KEY = Key.key("tinsel", "spacing");

    private final TextWidthMeasurer measurer = new TextWidthMeasurer(
            FontSet.of(Spacing.font())
    );

    private void assertSpacing(int spacing) {
        Assertions.assertEquals(
                spacing,
                measurer.measure(Spacing.spacing(spacing))
        );
    }

    @Test
    public void measureSinglePositive() {
        assertSpacing(1);
        assertSpacing(4);
        assertSpacing(16);
        assertSpacing(1024);
    }

    @Test
    public void measureSingleNegative() {
        assertSpacing(-1);
        assertSpacing(-4);
        assertSpacing(-16);
        assertSpacing(-1024);
    }

    @Test
    public void measureOutOfBounds() {
        Assertions.assertEquals(5, measurer.measure(Component.text("\uE00B").font(SPACING_FONT_KEY)));
        Assertions.assertEquals(5, measurer.measure(Component.text("\uF8F4").font(SPACING_FONT_KEY)));
    }
}
