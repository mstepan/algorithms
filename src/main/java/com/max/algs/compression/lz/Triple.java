package com.max.algs.compression.lz;


class Triple {

    final int offset;
    final int length;
    char ch;

    public Triple(int offset, int count, char ch) {
        this.offset = offset;
        this.length = count;
        this.ch = ch;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", offset, length, ch);
    }
}
