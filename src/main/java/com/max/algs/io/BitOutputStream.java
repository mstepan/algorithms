package com.max.algs.io;

import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Write single bit to output stream.
 *
 * @author Maksym Stepanenko.
 */
public class BitOutputStream extends OutputStream implements AutoCloseable {

    /**
     * If we treat byte is unsigned the possible range value: [0;255].
     * The closest value that divides by '8' (Byte.SIZE) without remaining is '248'.
     * So, in each chunk we can store 248 / 8 = 31 bytes.
     */
    private static final int CHUNK_SIZE = 31;
    private final OutputStream out;
    private final byte[] chunk = new byte[CHUNK_SIZE];

    private int chunkIndex;

    private int bitsInCurrentByteCount;
    private int totalBitsInChunk;


    public BitOutputStream(OutputStream out) {
        checkNotNull(out);
        this.out = out;
    }


    public void write(int bitValue, int bitsCount) throws IOException {
        for (int shift = bitsCount - 1; shift >= 0; shift--) {
            int singleBit = (bitValue & (1 << shift)) >>> shift;
            write(singleBit);
        }
    }


    @Override
    public void write(int bitValue) throws IOException {

        addBitToCurrentByte(bitValue);

        /* current byte filled */
        if (bitsInCurrentByteCount == Byte.SIZE) {

            bitsInCurrentByteCount = 0;
            ++chunkIndex;

            /* Chunk fully filled. Write to source stream */
            if (chunkIndex == chunk.length) {
                writeInMemoryData();
            }
        }
    }


    private void addBitToCurrentByte(int bitValue) {
        int filledByte = chunk[chunkIndex];

        filledByte <<= 1;
        filledByte |= bitValue & 1;

        chunk[chunkIndex] = (byte) filledByte;

        ++bitsInCurrentByteCount;
        ++totalBitsInChunk;
    }

    private void zeroChunk() {
        chunkIndex = 0;
        totalBitsInChunk = 0;
        for (int i = 0; i < chunk.length; i++) {
            chunk[i] = 0;
        }
    }

    private void writeInMemoryData() throws IOException {

        /* write bits count in chunk */
        out.write(totalBitsInChunk);

        /* write chunk of bytes itself */
        out.write(chunk, 0, chunkIndex < chunk.length ? chunkIndex + 1 : chunkIndex);
        out.flush();

        zeroChunk();
    }


    @Override
    public void close() throws IOException {
        writeInMemoryData();
        out.close();
    }

}
