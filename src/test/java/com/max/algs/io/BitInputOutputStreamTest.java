package com.max.algs.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class BitInputOutputStreamTest {


    @Test(expected = EOFException.class)
    public void readTwoChunks() throws IOException {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        StringBuilder bytesStr = new StringBuilder();

        for (int i = 0; i < 31; i++) {
            bytesStr.append("10101010");
        }

        bytesStr.append("11111111");
        bytesStr.append("1000");

        writeBinaryString(byteOut, bytesStr.toString());

        byte[] rawData = byteOut.toByteArray();

        try (BitInputStream in = new BitInputStream(new ByteArrayInputStream(rawData))) {

            for (int i = 0; i < 31; i++) {
                assertEquals(1, in.read());
                assertEquals(0, in.read());
                assertEquals(1, in.read());
                assertEquals(0, in.read());
                assertEquals(1, in.read());
                assertEquals(0, in.read());
                assertEquals(1, in.read());
                assertEquals(0, in.read());
            }

            for (int i = 0; i < 8; i++) {
                assertEquals(1, in.read());
            }

            assertEquals(1, in.read());
            assertEquals(0, in.read());
            assertEquals(0, in.read());
            assertEquals(0, in.read());

            in.read();
        }
    }


    @Test(expected = EOFException.class)
    public void readFromEmptyStream() throws IOException {


        try (BitInputStream in = new BitInputStream(new ByteArrayInputStream(new byte[0]))) {

            in.read();

        }
    }


    @Test(expected = EOFException.class)
    public void readOneChunk() throws IOException {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        writeBinaryString(byteOut, "0011_00111010");

        byte[] rawData = byteOut.toByteArray();

        try (BitInputStream in = new BitInputStream(new ByteArrayInputStream(rawData))) {

            assertEquals(0, in.read());
            assertEquals(0, in.read());
            assertEquals(1, in.read());
            assertEquals(1, in.read());

            assertEquals(0, in.read());
            assertEquals(0, in.read());
            assertEquals(1, in.read());
            assertEquals(1, in.read());
            assertEquals(1, in.read());
            assertEquals(0, in.read());
            assertEquals(1, in.read());
            assertEquals(0, in.read());

            in.read();
        }
    }

    @Test(expected = EOFException.class)
    public void readFullByte() throws IOException {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        writeBinaryString(byteOut, "0011_1010");

        byte[] rawData = byteOut.toByteArray();

        try (BitInputStream in = new BitInputStream(new ByteArrayInputStream(rawData))) {
            assertEquals(0, in.read());
            assertEquals(0, in.read());
            assertEquals(1, in.read());
            assertEquals(1, in.read());
            assertEquals(1, in.read());
            assertEquals(0, in.read());
            assertEquals(1, in.read());
            assertEquals(0, in.read());
            in.read();
        }
    }

    @Test
    public void readHalfByte() throws IOException {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        writeBinaryString(byteOut, "11_1010");

        byte[] rawData = byteOut.toByteArray();

        try (BitInputStream in = new BitInputStream(new ByteArrayInputStream(rawData))) {
            assertEquals(1, in.read());
            assertEquals(1, in.read());
            assertEquals(1, in.read());
            assertEquals(0, in.read());
            assertEquals(1, in.read());
            assertEquals(0, in.read());
        }
    }


    @Test
    public void writeTwoChunksOfBits() throws IOException {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < 31; i++) {
            buf.append("10101010");
        }

        buf.append("1111100");

        writeBinaryString(byteOut, buf.toString());

        byte[] rawData = byteOut.toByteArray();


        int offset = 0;
        assertEquals("Counter byte is incorrect", 248, (rawData[offset] & 0xFF));
        ++offset;

        for (int i = 0; i < 31; i++, offset++) {
            assertEquals("Bits data is incorrect", "10101010", Integer.toBinaryString(rawData[offset] & 0xFF));
        }

        assertEquals("Counter byte is incorrect", 7, (rawData[offset] & 0xFF));
        ++offset;
        assertEquals("Bits data is incorrect", "1111100", Integer.toBinaryString(rawData[offset] & 0xFF));
    }


    @Test
    public void writeOneChunkOfBits() throws IOException {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        writeBinaryString(byteOut, "10100001_00001111_11111111_1010");

        byte[] rawData = byteOut.toByteArray();

        assertEquals("Counter byte is incorrect", 28, rawData[0]);
        assertEquals("Bits data is incorrect", "10100001", Integer.toBinaryString(rawData[1] & 0xFF));
        assertEquals("Bits data is incorrect", "1111", Integer.toBinaryString(rawData[2] & 0xFF));
        assertEquals("Bits data is incorrect", "11111111", Integer.toBinaryString(rawData[3] & 0xFF));
        assertEquals("Bits data is incorrect", "1010", Integer.toBinaryString(rawData[4] & 0xFF));
    }


    @Test
    public void writeMultipleBitsAtOnce() throws IOException {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();

        try (BitOutputStream out = new BitOutputStream(byteOut)) {
            out.write(10, 4);
            out.write(15, 4);
        }

        byte[] rawData = byteOut.toByteArray();

        assertEquals("Counter byte is incorrect", 8, rawData[0]);
        assertEquals("Bits data is incorrect", "10101111", Integer.toBinaryString(rawData[1] & 0xFF));
    }


    private void writeBinaryString(ByteArrayOutputStream byteOut, String bits) throws IOException {
        try (BitOutputStream out = new BitOutputStream(byteOut)) {

            for (int i = 0; i < bits.length(); i++) {
                char ch = bits.charAt(i);
                if (ch == '0' || ch == '1') {
                    out.write(ch == '0' ? 0 : 1);
                }
            }
        }
    }


}
