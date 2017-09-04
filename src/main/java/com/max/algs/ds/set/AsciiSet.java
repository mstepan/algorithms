package com.max.algs.ds.set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Store set of ASCII characters.
 * <p>
 * Use constant space. O(1)
 */
public class AsciiSet {
    private static final int MIN_ASCII_CODE = 0;
    private static final int MAX_ASCII_CODE = 255;

    private static final int ASCII_CHARS_COUNT = MAX_ASCII_CODE + 1;

    private static final int BITS_PER_BUCKET = Long.SIZE;

    private static final int BUCKETS_COUNT = ASCII_CHARS_COUNT / BITS_PER_BUCKET;

    private final long[] arr = new long[BUCKETS_COUNT];

    /**
     * time: O(1)
     */
    public boolean add(char ch) {
        int code = toCode(ch);

        if (contains(ch)) {
            return true;
        }

        arr[bucket(code)] |= (1 << offset(code));
        return false;
    }

    /**
     * time: O(1)
     */
    public boolean contains(char ch) {
        int code = toCode(ch);
        return (arr[bucket(code)] & (1 << offset(code))) != 0;
    }

    private static int offset(int code) {
        return code % BITS_PER_BUCKET;
    }

    private static int bucket(int code) {
        return code / BITS_PER_BUCKET;
    }

    private static int toCode(char ch) {
        int code = (int) ch;
        checkArgument(code >= MIN_ASCII_CODE && code <= MAX_ASCII_CODE, "Incorrect ASCII character passed '%s'", ch);
        return code;
    }
}
