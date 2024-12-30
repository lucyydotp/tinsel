package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A basic {@link FontSet} implementation.
 */
@NullMarked
final class FontSetImpl implements FontSet {
    private final Map<Key, FontFamily> fontFamilies;
    private final Map<Key, Font> fonts;
    private final FontFamily defaultFont;

    /**
     * @param fontFamilies the families in this set, keyed by font key
     * @param fonts the fonts contained within this set's families, keyed by name
     */
    FontSetImpl(Map<Key, FontFamily> fontFamilies, Map<Key, Font> fonts) {
        this.fontFamilies = fontFamilies;
        this.fonts = fonts;
        this.defaultFont = fontFamilies.getOrDefault(FontFamily.DEFAULT_FONT_KEY, FontFamily.vanilla());
    }

    static FontSetImpl ofFamilies(Iterable<FontFamily> families) {
        final var familyMap = new HashMap<Key, FontFamily>();
        final var fonts = new HashMap<Key, Font>();

        for (final var family : families) {
            if (familyMap.put(family.key(), family) != null) {
                throw new IllegalArgumentException("Duplicate font family key " + family.key());
            }

            for (final var fontOffset : family.offsets()) {
                final var font = Objects.requireNonNull(family.offset(fontOffset));
                if (fonts.put(font.name(), font) != null) {
                    throw new IllegalArgumentException("Duplicate font key " + font.name());
                }
            }
        }

        return new FontSetImpl(Collections.unmodifiableMap(familyMap), Collections.unmodifiableMap(fonts));
    }


    @Override
    public @Nullable Font font(Key font) {
        return fonts.get(font);
    }

    @Override
    public Map<Key, FontFamily> fontFamilies() {
        return fontFamilies;
    }

    @Override
    public FontFamily defaultFont() {
        return defaultFont;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (FontSetImpl) obj;
        return Objects.equals(this.fontFamilies, that.fontFamilies) &&
                Objects.equals(this.fonts, that.fonts);
    }
}
