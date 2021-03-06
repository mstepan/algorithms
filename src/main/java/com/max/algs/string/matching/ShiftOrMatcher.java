package com.max.algs.string.matching;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Substring matching using 'shift-or' algorithm.
 */
public final class ShiftOrMatcher {

    private static final int MAX_PATTERN_LENGTH = 32;

    private ShiftOrMatcher() {
        throw new IllegalStateException("Can't instantiate utility only class");
    }

    /**
     * Substring matching using 'shift-or' algorithm.
     */
    public static int find(String pattern, String text) {

        checkArgument(pattern != null, "null 'pattern' passed");
        checkArgument(text != null, "null 'text' passed");
        checkArgument(pattern.length() <= MAX_PATTERN_LENGTH);

        int patternMask = 1 << (pattern.length() - 1);

        Map<Character, Integer> map = createInvertedBitMap(pattern);

        int curMask = -1;
        char ch;

        for (int i = 0, textLength = text.length(); i < textLength; ++i) {
            ch = text.charAt(i);

            curMask = (curMask << 1) | map.getOrDefault(ch, -1);

            if ((curMask & patternMask) == 0) {
                // occurrence found
                return i - (pattern.length() - 1);
            }
        }

        return -1;
    }

    private static Map<Character, Integer> createInvertedBitMap(String str) {
        Map<Character, Integer> bitMap = new HashMap<>();
        char ch;

        for (int i = 0, strLength = str.length(); i < strLength; ++i) {
            ch = str.charAt(i);
            bitMap.put(ch, bitMap.getOrDefault(ch, 0) | (1 << i));
        }

        // create complement/inverted 'bitMap'
        for (Map.Entry<Character, Integer> entry : bitMap.entrySet()) {
            entry.setValue(~entry.getValue());
        }

        return bitMap;
    }


}
