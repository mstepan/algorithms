package com.max.algs.compression.lz;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * LZ77 compression/decompression.
 * <p>
 * Profile option:
 * -agentlib:hprof=cpu=times,file=/Users/mstepan/repo/incubator/algorithms/src/main/java/com/max/algs/compression/lz/java.hprof.txt
 */
public class LZ77Main {

    private static final Logger LOG = Logger.getLogger(LZ77Main.class);

    private static final int NANOS_IN_MS = 1_000_000;

    private static final int SEARCH_BUF_SIZE = 2048;
    private static final int LOOK_AHEAD_BUF_SIZE = 128;

    private LZ77Main() throws IOException {

        long startTime = System.nanoTime();

        String inPath =
                "/Users/mstepan/repo/incubator/algorithms/src/main/java/com/max/algs/compression/lz/in-big.txt";

        String archivePath =
                "/Users/mstepan/repo/incubator/algorithms/src/main/java/com/max/algs/compression/lz/compressed.data";

        compress(inPath, archivePath);

        String decompressedPath =
                "/Users/mstepan/repo/incubator/algorithms/src/main/java/com/max/algs/compression/lz/in2.txt";

        decompress(archivePath, decompressedPath);

        long endTime = System.nanoTime();

        LOG.info("Compression done, time: " + (endTime - startTime) / NANOS_IN_MS + " ms");
    }

    public static void compress(String inPathStr, String outPathStr) throws IOException {

        File inFile = new File(inPathStr);

        if (!inFile.exists()) {
            throw new IllegalArgumentException("File '" + inPathStr + "' doesn't exists");
        }

        Path outPath = Paths.get(outPathStr);
        Files.deleteIfExists(outPath);
        Files.createFile(outPath);

        int maxOffset = 0;
        int maxLength = 0;

        SlidingWindowIterator window = new SlidingWindowIterator(Files.newBufferedReader(inFile.toPath()),
                SEARCH_BUF_SIZE, LOOK_AHEAD_BUF_SIZE);

        try (DataOutputStream out = new DataOutputStream(Files.newOutputStream(outPath))) {

            while (window.hasNext()) {
                Triple triple = window.next();

                maxOffset = Math.max(maxOffset, triple.offset);
                maxLength = Math.max(maxLength, triple.length);

                write(out, triple);
            }
        }

        LOG.info("maxOffset: " + maxOffset + ", maxLength: " + maxLength);
    }

    public static void decompress(String archiveStr, String outPathStr) throws IOException {

        File inFile = new File(archiveStr);

        if (!inFile.exists()) {
            throw new IllegalArgumentException("Archive file '" + archiveStr + "' doesn't exists");
        }

        Path outPath = Paths.get(outPathStr);
        Files.deleteIfExists(outPath);
        Files.createFile(outPath);

        SearchBuffer searchBuf = new SearchBuffer(SEARCH_BUF_SIZE);

        try (DataInputStream in = new DataInputStream(Files.newInputStream(inFile.toPath()));
             BufferedWriter out = Files.newBufferedWriter(outPath)) {

            while (in.available() != 0) {

                try {
                    Triple triple = read(in);

                    if (triple.offset != 0) {
                        for (int i = 0; i < triple.length; i++) {

                            int index = searchBuf.length() - triple.offset;

                            Iterator<Character> decodedChIt = searchBuf.getIteratorAtIndex(index);

                            char decodedCh = decodedChIt.next();

                            out.write(decodedCh);

                            searchBuf.add(decodedCh);
                        }
                    }

                    out.write(triple.ch);
                    searchBuf.add(triple.ch);
                }
                catch (EOFException eofEx) {
                    break;
                }
            }
        }
    }

    private static void write(DataOutputStream out, Triple triple) throws IOException {
        out.writeShort(triple.offset);
        out.writeByte(triple.length);
        out.writeChar(triple.ch);
    }

    private static Triple read(DataInputStream in) throws IOException {
        return new Triple(in.readShort(), in.readByte(), in.readChar());
    }

    public static void main(String[] args) {
        try {
            new LZ77Main();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
