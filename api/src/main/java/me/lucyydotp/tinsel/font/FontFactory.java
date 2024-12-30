package me.lucyydotp.tinsel.font;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads fonts from various sources.
 */
@NullMarked
class FontFactory {
    static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Key.class, new KeySerializer())
            .create();

    private static class KeySerializer extends TypeAdapter<Key> {
        @Override
        public void write(JsonWriter out, Key value) throws IOException {
            out.value(value.asString());
        }

        @Override
        public Key read(JsonReader in) throws IOException {
            return Key.key(in.nextString());
        }
    }

    private FontFactory() {
    }

    /**
     * A font definition, for serialization to/from a JSON file.
     *
     * @param key     the font's key
     * @param chars   a map of characters and their width
     * @param offsets the font's offsets
     */
    private record FontDefinition(
            Key key,
            Map<String, Integer> chars,
            Map<Integer, Key> offsets
    ) {
    }

    /**
     * Creates a {@link FontFamily} from a pre-generated font definition file.
     *
     * @param stream an input stream to read the font file
     */
    static FontFamily fromJson(InputStream stream) throws IOException {
        final FontDefinition file;
        try (final var reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            file = gson.fromJson(reader, FontDefinition.class);
        }

        final var codePointsMap = new HashMap<Integer, Integer>();
        for (final var entry : file.chars.entrySet()) {
            codePointsMap.put(Character.codePointAt(entry.getKey(), 0), entry.getValue());
        }

        return FontFamilyImpl.of(
                file.key,
                Collections.unmodifiableMap(codePointsMap),
                file.offsets
        );
    }

    /**
     * Creates a font family using the vanilla definition with offsets.
     *
     * @param offsets the offset fonts to use
     */
    @Contract(pure = true, value = "_ -> new")
    static FontFamily vanillaWithOffsets(Map<Integer, Key> offsets) {
        if (!offsets.containsKey(0)) {
            offsets = new HashMap<>(offsets);
            offsets.put(0, FontFamily.DEFAULT_FONT_KEY);
        }

        return FontFamilyImpl.of(
                FontFamilyImpl.VANILLA_FONT.key(),
                FontFamilyImpl.VANILLA_FONT.charWidthMap(),
                offsets
        );
    }
}
