package me.lucyydotp.tinsel.util;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for UTF-8 encoded strings.
 */
public class Utf8Util {
    /**
     * Splits a string into a list of Unicode code points.
     * @param string the string to split
     */
    @Contract(pure = true)
    public static List<Integer> codePoints(String string) {
        final var out = new ArrayList<Integer>();

        var highSurrogate = ' ';
        for (final var chr : string.toCharArray()) {
            if (Character.isHighSurrogate(chr)) {
                highSurrogate = chr;
            } else if (Character.isLowSurrogate(chr)) {
                out.add(Character.toCodePoint(highSurrogate, chr));
            } else {
                out.add((int) chr);
            }
        }
        return out;
    }
}
