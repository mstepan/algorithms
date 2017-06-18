package com.max.algs.compression.huffman;

class PrefixCode {


    final int bits;
    final int length;

    PrefixCode(int bits, int length) {
        super();
        this.bits = bits;
        this.length = length;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        String binaryStr = Integer.toBinaryString(bits);

        int zerosToAdd = length - binaryStr.length();

        for (int i = 0; i < zerosToAdd; i++) {
            buf.append('0');
        }

        buf.append(binaryStr);
        return buf.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bits;
        result = prime * result + length;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PrefixCode other = (PrefixCode) obj;
        if (bits != other.bits)
            return false;
        if (length != other.length)
            return false;
        return true;
    }


}
