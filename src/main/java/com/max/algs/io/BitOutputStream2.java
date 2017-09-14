package com.max.algs.io;

import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Write single bit to output stream.
 *
 * @author Maksym Stepanenko.
 */
public class BitOutputStream2 extends OutputStream implements AutoCloseable {

    private final OutputStream out;

    private final StringBuilder traceBuf;

    private int value;
    private int bitsCount;

    public BitOutputStream2(OutputStream out) {
        checkNotNull(out);
        this.out = out;
        this.traceBuf = new StringBuilder();
    }

    @Override
    public void write(int bitValue) throws IOException {

        traceBuf.append(bitValue & 1);

        value <<= 1;
        value |= (bitValue & 1);

        ++bitsCount;

        if (bitsCount == Byte.SIZE) {
            out.write(value);
            value = 0;
            bitsCount = 0;
        }
    }


    @Override
    public void close() throws IOException {
        if (bitsCount != 0) {
            out.write(value);
        }
        out.close();
    }

    @Override
    public String toString() {
        return traceBuf.toString();
    }

}
