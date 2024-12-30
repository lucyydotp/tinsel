package me.lucyydotp.tinsel.font;

import com.google.gson.JsonObject;
import me.lucyydotp.tinsel.util.Utf8Util;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.util.*;

@NullMarked
class ResourcePackBuilderImpl implements FontFamily.ResourcePackBuilder {

    private final FileSystem fs;
    private final Set<FontFamily> fonts = new HashSet<>();

    ResourcePackBuilderImpl(FileSystem fs) {
        this.fs = fs;
    }

    /**
     * Parses a provider JSON file and measures the width of its characters.
     * @param map the map to write measured characters to
     * @param provider the provider
     */
    private void parseJsonProvider(Map<Integer, Integer> map, JsonObject provider) throws IOException {
        switch (provider.get("type").getAsString()) {
            case "space" -> {
                for (final var entry : provider.getAsJsonObject("advances").entrySet()) {
                    map.put(Character.codePointAt(entry.getKey(), 0), entry.getValue().getAsInt());
                }
            }

            case "bitmap" -> {
                final var imageId = provider.get("file").getAsString().split(":");
                final BufferedImage image;
                try (final var is = Files.newInputStream(fs.getPath("assets", imageId[0], "textures", imageId[1]))) {
                    image = ImageIO.read(is);
                }

                final var rows = provider.getAsJsonArray("chars");
                final var rowHeight = image.getHeight() / rows.size();

                for (int row = 0; row < rows.size(); row++) {
                    final var glyphs = Utf8Util.codePoints(rows.get(row).getAsString());
                    final var columnWidth = image.getWidth() / glyphs.size();
                    final var y = row * rowHeight;

                    glyphs:
                    for (int index = 0; index < glyphs.size(); index++) {
                        final var chr = glyphs.get(index);
                        if (chr == 0) continue;

                        // Start at the rightmost column, scan backwards for a non-translucent pixel
                        final var x = columnWidth * index;
                        var dx = x + columnWidth - 1;
                        while (dx - x >= 0) {
                            var rowIsEmpty = true;
                            for (int i = y; i < y + rowHeight; i++) {
                                if ((image.getRGB(dx, i) >>> 24) != 0) {
                                    rowIsEmpty = false;
                                    break;
                                }
                            }
                            if (rowIsEmpty) {
                                dx--;
                            } else {
                                // add 1 to include the current row, 1 for letter spacing
                                map.put(chr, dx - x + 2);
                                continue glyphs;
                            }
                        }
                        // TODO(lucy): unhandled glyph, log here
                    }
                }
            }
            // TODO(lucy): ttf

            default -> throw new IllegalArgumentException(
                    "Unknown glyph provider type " + provider.get("type").getAsString()
            );
        }
    }


    @Override
    public FontFamily.ResourcePackBuilder add(Key fontKey, Map<Integer, Key> offsets) {
        final Map<Integer, Integer> chars = new HashMap<>();
        try (final var defReader = Files.newBufferedReader(fs.getPath("assets", fontKey.namespace(), "font", fontKey.value() + ".json"))) {
            final var providers = FontFactory.gson.fromJson(defReader, JsonObject.class).getAsJsonArray("providers");
            for (final var provider : providers) {
                parseJsonProvider(chars, provider.getAsJsonObject());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fonts.add(FontFamilyImpl.of(
                fontKey,
                Collections.unmodifiableMap(chars),
                offsets
        ));
        return this;
    }

    @Override
    public Set<FontFamily> build() {
        return Collections.unmodifiableSet(fonts);
    }
}
