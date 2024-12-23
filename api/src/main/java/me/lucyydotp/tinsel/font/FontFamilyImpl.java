package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A basic {@link FontFamily} implementation.
 *
 * @param key          the font's key
 * @param charWidthMap a map of the font's characters to their width
 * @param fonts        a map of fonts, keyed by their offset
 */
@NullMarked
record FontFamilyImpl(Key key, Map<Integer, Integer> charWidthMap, Map<Integer, Font> fonts) implements FontFamily {
    static FontFamilyImpl VANILLA_FONT;

    static {
        try {
            VANILLA_FONT = (FontFamilyImpl) FontFactory.fromJson(FontFamilyImpl.class.getResourceAsStream("/tinsel/default_font.json"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    FontFamilyImpl {
        // TODO(lucy): is this the behaviour we want? would just adding it if it's not there already be better?
        if (!fonts.containsKey(0)) {
            throw new IllegalArgumentException(String.format("Font family '%s' has no font mapping for width 0", key.asString()));
        }
    }

    static FontFamilyImpl of(Key key, Map<Integer, Integer> charWidthMap, Map<Integer, Key> fontOffsets) {
        final var fontMap = new HashMap<Integer, Font>();

        for (final var offset : fontOffsets.entrySet()) {
            fontMap.put(offset.getKey(), new FontImpl(
                    offset.getValue(),
                    key,
                    offset.getKey()
            ));
        }

        return new FontFamilyImpl(
                key,
                charWidthMap,
                Collections.unmodifiableMap(fontMap)
        );
    }

    @Override
    public Set<Integer> offsets() {
        return fonts.keySet();
    }

    @Override
    public @Nullable Font offset(int offset) {
        return fonts.get(offset);
    }

    @Override
    public int measureCharacter(int character) {
        return charWidthMap.getOrDefault(character, FontFamily.PLACEHOLDER_CHARACTER_WIDTH);
    }
}
