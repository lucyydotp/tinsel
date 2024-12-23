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
 * @param fontFamilies the families in this set, keyed by font key
 * @param fonts the fonts contained within this set's families, keyed by name
 */
@NullMarked
record FontSetImpl(Map<Key, FontFamily> fontFamilies, Map<Key, Font> fonts) implements FontSet {

    static FontSetImpl ofFamilies(FontFamily... families) {
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
}
