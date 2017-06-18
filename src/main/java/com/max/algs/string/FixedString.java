package com.max.algs.string;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Same as java.lang.String class, but with enhanced 'substring' method
 * as it was implemented before jdk-7.
 */
public final class FixedString implements CharSequence, Comparable<FixedString>, Serializable {


    public static final Comparator<FixedString> LEXICOGRAPHICAL_CMP = new Comparator<FixedString>() {

        @Override
        public int compare(FixedString o1, FixedString o2) {

            char[] value1 = o1.value;
            char[] value2 = o2.value;

            int minLength = Math.min(o1.length, o2.length);

            for (int i = 0; i < minLength; i++) {
                if (value1[o1.offset + i] > value2[o2.offset + i]) {
                    return 1;
                }

                if (value1[o1.offset + i] < value2[o2.offset + i]) {
                    return -1;
                }
            }

            if (o1.length > o2.length) {
                return 1;
            }

            if (o1.length < o2.length) {
                return -1;
            }

            return 0;
        }

    };

    private static final long serialVersionUID = 6691249189517466100L;

    private final char[] value;
    private final int length;
    private int offset;
    private int hash;


    public FixedString(String str) {
        if (str == null) {
            throw new IllegalArgumentException("NULL 'str' parameter passed");
        }
        value = str.toCharArray();
        length = value.length;
    }

    private FixedString(char[] value, int offset) {
        this(value, offset, value.length - 1);
    }

    private FixedString(char[] value, int start, int end) {
        this.value = value;
        this.offset = start;
        this.length = end - start + 1;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        checkBoundaries(index);
        return value[index];
    }


    private void checkBoundaries(int index) {
        if (index < 0 || index >= length || index < offset) {
            throw new IndexOutOfBoundsException(
                    String.format("'index' out of bounds: %d , should be in range [%d, %d]", index, offset, (length - offset)));
        }
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new FixedString(value, start, end);
    }

    public FixedString substring(int index) {
        return new FixedString(value, index);
    }

    @Override
    public int compareTo(FixedString other) {
        return LEXICOGRAPHICAL_CMP.compare(this, other);
    }

    @Override
    public int hashCode() {

        if (hash == 0) {
            final int prime = 31;
            int result = 17;

            for (int i = offset; i < length; i++) {
                result = prime * result + value[i];
            }

            hash = result;
        }

        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FixedString other = (FixedString) obj;

        if (length != other.length || offset != other.offset) {
            return false;
        }

        for (int i = offset; i < length; i++) {
            if (value[i] != other.value[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return new String(value, offset, length);
    }

}
