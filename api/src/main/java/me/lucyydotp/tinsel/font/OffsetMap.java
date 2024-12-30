package me.lucyydotp.tinsel.font;

import net.kyori.adventure.key.Key;

import java.util.HashMap;
import java.util.Map;

public class OffsetMap {
    public static Map<Integer, Key> offsets(Key key, int... offsets) {
        final var map = new HashMap<Integer, Key>();

        for (final var offset : offsets) {
            map.put(offset, Key.key(key, key.value() + "_offset_" + offset));
        }
        return map;
    }
}
