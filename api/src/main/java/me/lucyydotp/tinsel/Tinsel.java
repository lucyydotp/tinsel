package me.lucyydotp.tinsel;

import me.lucyydotp.tinsel.font.FontFamily;
import me.lucyydotp.tinsel.font.FontSet;
import me.lucyydotp.tinsel.font.OffsetMap;
import me.lucyydotp.tinsel.font.Spacing;
import me.lucyydotp.tinsel.measurement.TextWidthMeasurer;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Contract;

import java.util.HashSet;
import java.util.Set;

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
