package com.max.algs.io;

import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;


public class BitInputStream extends InputStream {

    private final InputStream inStream;

    public BitInputStream(InputStream in) {
        checkNotNull(in);
        this.inStream = in;
    }

    @Override
    public int read() throws IOException {
        return -1;
    }

}
