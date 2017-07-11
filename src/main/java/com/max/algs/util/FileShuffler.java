package com.max.algs.util;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Random shuffle a big inFile of integer values.
 * Use up to 1GB of RAM.
 */
public final class FileShuffler {

    private static final Logger LOG = Logger.getLogger(FileShuffler.class);

    // we can store 250 mln integers in 1GB of RAM
    private static final int INTS_COUNT_IN_GB = 250_000_000;

    private static final Random RAND = ThreadLocalRandom.current();

    private final File inFile;
    private final File outFile;

    public FileShuffler(Path inPath, Path outPath) {
        checkNotNull(inPath);
        checkNotNull(outPath);
        checkArgument(inPath.toFile().exists(), "File not exists with path: '%s'", inPath.toString());
        this.inFile = inPath.toFile();
        this.outFile = outPath.toFile();
    }

    /**
     * Shuffle inFile content in-memory using Fisher-Yates shuffling.
     */
    private static void shuffleInMemoryAndWriteToOutputFile(File singleFile, File fileToAppend) {
        LOG.info("shuffleInMemory and append: " + singleFile.getPath());

        int[] data = readIntsFromFile(singleFile);

        randomShuffleArray(data);

        appendIntsToFile(data, fileToAppend);
    }

    private static void randomShuffleArray(int[] data) {
        int swapIndex;
        int temp;
        for (int i = 0; i < data.length - 1; ++i) {
            swapIndex = i + RAND.nextInt(data.length - i);

            temp = data[i];
            data[i] = data[swapIndex];
            data[swapIndex] = temp;
        }
    }

    private static int[] readIntsFromFile(File file) {

        int[] data = new int[(int) (file.length() / Integer.BYTES)];

        try (FileInputStream fileIn = new FileInputStream(file);
             BufferedInputStream bufIn = new BufferedInputStream(fileIn);
             DataInputStream dataIn = new DataInputStream(bufIn)) {

            int index = 0;
            while (dataIn.available() >= Integer.BYTES) {
                data[index] = dataIn.readInt();
                ++index;
            }
        }
        catch (IOException ioEx) {
            handleException(ioEx);
        }
        return data;
    }

    private static void appendIntsToFile(int[] data, File file) {
        try (FileOutputStream fileOut = new FileOutputStream(file, true);
             BufferedOutputStream bufOut = new BufferedOutputStream(fileOut);
             DataOutputStream dataOut = new DataOutputStream(bufOut)) {
            for (int value : data) {
                dataOut.writeInt(value);
            }
        }
        catch (IOException ioEx) {
            handleException(ioEx);
        }
    }

    private static File createTempFile(int index) {
        try {
            File file = File.createTempFile("random-shuffle-" + index + "-chunk", ".tmp");
            LOG.info(file.getAbsolutePath());
            return file;
        }
        catch (IOException ioEx) {
            handleException(ioEx);
        }
        return null;
    }

    private static DataOutputStream createDataOutputStream(File file) {
        try {
            return new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        }
        catch (FileNotFoundException fileNotFoundEx) {
            handleException(fileNotFoundEx);
        }
        return null;
    }

    private static void closeStreams(Closeable[] streams) {

        for (Closeable stream : streams) {
            try {
                stream.close();
            }
            catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
    }

    private static void handleException(IOException ioEx) {
        throw new IllegalStateException(ioEx);
    }

    /* MAIN */
    public static void main(String[] args) {
        Path inFile = Paths.get("/Users/mstepan/Desktop/in.dat");
        Path outFile = Paths.get("/Users/mstepan/Desktop/out.dat");

        FileShuffler shuffler = new FileShuffler(inFile, outFile);
        shuffler.shuffle();

        LOG.info("FileShuffler done: java-" + System.getProperty("java.version"));
    }

    /**
     * Do random shuffle.
     */
    public void shuffle() {

        int temporaryFilesCount = calculateTempFilesCount();

        LOG.info("temporaryFilesCount: " + temporaryFilesCount);

        File[] tempFiles = createTempFiles(temporaryFilesCount);

        distributeDataAcrossFiles(tempFiles);

        for (File singleTempFile : tempFiles) {
            shuffleInMemoryAndWriteToOutputFile(singleTempFile, outFile);
        }
    }

    private void distributeDataAcrossFiles(File[] tempFiles) {
        DataOutputStream[] streams = createDataOutputStreams(tempFiles);

        try {
            try {
                try (FileInputStream inStream = new FileInputStream(inFile);
                     BufferedInputStream bufferedIn = new BufferedInputStream(inStream);
                     DataInputStream dataStream = new DataInputStream(bufferedIn)) {

                    while (dataStream.available() >= Integer.BYTES) {
                        streams[RAND.nextInt(streams.length)].writeInt(dataStream.readInt());
                    }
                }
            }
            catch (IOException ioEx) {
                throw new IllegalStateException(ioEx);
            }
        }
        finally {
            closeStreams(streams);
        }
    }

    private int calculateTempFilesCount() {
        int temporaryFilesCount = (int) (inFile.length() / INTS_COUNT_IN_GB);

        if (inFile.length() % INTS_COUNT_IN_GB != 0) {
            ++temporaryFilesCount;
        }

        return temporaryFilesCount;
    }

    private File[] createTempFiles(int temporaryFilesCount) {
        File[] tempFiles = new File[temporaryFilesCount];
        for (int i = 0; i < tempFiles.length; ++i) {
            tempFiles[i] = createTempFile(i);
        }

        return tempFiles;
    }

    private DataOutputStream[] createDataOutputStreams(File[] tempFiles) {

        DataOutputStream[] streams = new DataOutputStream[tempFiles.length];
        for (int i = 0; i < tempFiles.length; ++i) {
            streams[i] = createDataOutputStream(tempFiles[i]);
        }

        return streams;
    }

}
