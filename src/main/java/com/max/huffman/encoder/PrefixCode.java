package com.max.huffman.encoder;


final class PrefixCode {

    private final int value;
    private final int bitsCount;

    PrefixCode(int value, int count) {
        this.value = value;
        this.bitsCount = count;
    }

    public int getValue() {
        return value;
    }

    public int getBitsCount() {
        return bitsCount;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(bitsCount);

        int bitsMask = value;

        for (int i = 0; i < bitsCount; ++i) {
            buf.append(bitsMask & 1);
            bitsMask >>= 1;
        }

        return buf.reverse().toString();
    }
}
