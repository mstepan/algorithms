package com.max.algs.file;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkArgument;

public final class ReverseFileMain {


    private static final int CHUNK_SIZE = 4096;

    private ReverseFileMain() throws Exception {

        Path originalPath = Paths.get("/Users/mstepan/repo/incubator/algorithms/src/main/java/com/max/algs/file/1.txt");
        Path invertedPath = Paths.get("/Users/mstepan/repo/incubator/algorithms/src/main/java/com/max/algs/file/1_inverted" +
                                              ".txt");

        reverseFile(originalPath, invertedPath);

        Path restoredPath =
                Paths.get("/Users/mstepan/repo/incubator/algorithms/src/main/java/com/max/algs/file/1_restored.txt");

        reverseFile(invertedPath, restoredPath);

    }

    public static void reverseFile(Path inPath, Path outPath) throws IOException {

        checkArgument(Files.exists(inPath), "File not found %s", inPath);

        if (!Files.exists(outPath)) {
            Files.createFile(outPath);
        }

        try (RandomAccessFile inFile = new RandomAccessFile(inPath.toFile(), "r");
             BufferedWriter writer = Files.newBufferedWriter(outPath)) {

            FileChannel inChannel = inFile.getChannel();

            MappedByteBuffer mappedRegion;
            char[] buf = new char[CHUNK_SIZE];
            long prevFrom = inChannel.size();

            long from;
            int length;

            while (prevFrom != 0L) {

                from = Math.max(prevFrom - CHUNK_SIZE, 0L);
                length = (int) (prevFrom - from);

                mappedRegion = inChannel.map(FileChannel.MapMode.READ_ONLY, from, length);

                for (int i = 0; i < length; i++) {
                    buf[i] = (char) mappedRegion.get(i);
                }

                reverseInPlace(buf, length);

                writer.write(buf, 0, length);

                prevFrom = from;
            }
        }
    }

    private static void reverseInPlace(char[] data, int length) {
        int left = 0;
        int right = length - 1;

        while (left < right) {
            char temp = data[left];

            data[left] = data[right];
            data[right] = temp;

            ++left;
            --right;
        }
    }


    public static void main(String[] args) {
        try {
            new ReverseFileMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}

