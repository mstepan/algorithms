package com.max.algs.io;


import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import static com.google.common.base.Preconditions.checkNotNull;


public class BitInputStream2 extends InputStream {

    private final RandomAccessFile inStream;

    private final StringBuilder traceBuf;

    // single byte value
    private int value;

    // offset within byte value [0;7]
    private int offset = Byte.SIZE;

    public BitInputStream2(RandomAccessFile in) {
        checkNotNull(in);
        this.inStream = in;
        this.traceBuf = new StringBuilder();
    }

    @Override
    public int read() throws IOException {
        if (offset == Byte.SIZE) {
            readNextByte();

            if (value == -1) {
                return -1;
            }
        }

        int bitValue = value & 1;

        traceBuf.append(bitValue);

        value >>>= 1;
        ++offset;

        return bitValue;
    }

    private void readNextByte() {
        try {
            value = inStream.read();
            offset = 0;
        }
        catch (IOException ioEx) {
            throw new IllegalStateException(ioEx);
        }
    }

    @Override
    public String toString() {
        return traceBuf.toString();
    }

}
