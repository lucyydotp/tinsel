package me.lucyydotp.tinsel;

import me.lucyydotp.tinsel.font.FontFamily;
import me.lucyydotp.tinsel.font.FontSet;
import me.lucyydotp.tinsel.font.Spacing;
import me.lucyydotp.tinsel.measurement.TextWidthMeasurer;
import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;

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

    private static final int[] TINSEL_PACK_OFFSETS = new int[]{
            -4, -8, -12, -16, -20, -24, -28, -32, -36, -40, -44, -48,
            4, 8, 12, 16, 20, 24, 28, 32, 36, 40, 44, 48
    };

    /**
     * Creates a Tinsel instance, configured to use the Tinsel-provided resource pack.
     */
    @Contract(pure = true)
    public static Tinsel withTinselPack() {
        final var offsetMap = new HashMap<Integer, Key>();
        for (@Subst("0") final var offset : TINSEL_PACK_OFFSETS) {
            offsetMap.put(offset, Key.key("tinsel", "default_offset_" + offset));
        }

        return new Tinsel(
                FontSet.of(
                        FontFamily.vanillaWithOffsets(offsetMap),
                        Spacing.font()
                )
        );
    }
}
