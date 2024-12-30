package me.lucyydotp.tinsel;

import me.lucyydotp.tinsel.font.FontFamily;
import me.lucyydotp.tinsel.font.FontSet;
import me.lucyydotp.tinsel.font.OffsetMap;
import me.lucyydotp.tinsel.font.Spacing;
import me.lucyydotp.tinsel.layout.BasicDrawContextImpl;
import me.lucyydotp.tinsel.layout.TextDrawContext;
import me.lucyydotp.tinsel.measurement.TextWidthMeasurer;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * An instance of Tinsel.
 */
public class Tinsel {
    private final FontSet fonts;
    private final TextWidthMeasurer textWidthMeasurer;

    public Tinsel(FontSet fonts) {
        this.fonts = fonts;
        this.textWidthMeasurer = new TextWidthMeasurer(fonts);
    }

    public FontSet fonts() {
        return fonts;
    }

    public TextWidthMeasurer textWidthMeasurer() {
        return textWidthMeasurer;
    }

    /**
     * Draws some text.
     * @param totalWidth the total width of the output text. The component will be padded to be exactly of this width after building
     * @param baseStyle the style to apply to the root component. May be {@link Style#empty()}.
     * @param fn the function to draw the text with. Will be invoked in-place exactly once.
     * @return the drawn text
     */
    public Component draw(int totalWidth, Style baseStyle, Consumer<TextDrawContext> fn) {
        final var context = new BasicDrawContextImpl(this, totalWidth, baseStyle);
        fn.accept(context);
        return context.output();
    }

    /**
     * Creates a new builder to build a new {@link Tinsel} instance.
     */
    @Contract("-> new")
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builds {@link Tinsel} instances.
     */
    public static class Builder {

        private Builder() {
        }

        private final Set<FontFamily> fonts = new HashSet<>();

        private static final int[] TINSEL_PACK_VANILLA_OFFSETS = new int[]{
                -4, -8, -12, -16, -20, -24, -28, -32, -36, -40, -44, -48,
                4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48
        };

        /**
         * Configures the builder to use the Tinsel-provided resource pack.
         * @return the builder
         */
        @Contract(" -> this")
        public Builder withTinselPack() {
            withFont(FontFamily.vanillaWithOffsets(
                    OffsetMap.offsets(Key.key("tinsel", "default"), TINSEL_PACK_VANILLA_OFFSETS)
            ));
            withFont(Spacing.font());
            return this;
        }

        /**
         * Adds a font family to the builder.
         * @return the builder
         */
        @Contract("_ -> this")
        public Builder withFont(FontFamily font) {
            fonts.add(font);
            return this;
        }

        /**
         * Adds a collection of font families to the builder.
         * @return the builder
         */
        @Contract("_ -> this")
        public Builder withFonts(Iterable<FontFamily> fonts) {
            for (final var font : fonts) {
                withFont(font);
            }
            return this;
        }

        /**
         * Creates a new Tinsel instance from this builder.
         */
        @Contract(pure = true, value = "-> new")
        public Tinsel build() {
            return new Tinsel(FontSet.of(fonts));
        }
    }
}
