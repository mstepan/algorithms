package com.max.huffman.encoder;


final class PrefixCode {

    private final int value;
    private final int count;


    PrefixCode(int value, int count) {
        this.value = value;
        this.count = count;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(count);

        int bitsMask = value;

        for (int i = 0; i < count; ++i) {
            buf.append(bitsMask & 1);
            bitsMask >>= 1;
        }

        return buf.reverse().toString();
    }
}
