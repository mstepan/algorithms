package com.max.algs.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Different utilities related to the files/directories.
 */
public final class FileUtils {


    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};


    /**
     * Generate file with all possible integer values, aka in range [Integer.MIN_VALUE; Integer.MAX_VALUE]
     */
    public static void generateAllIntsFile(Path inFile) throws IOException {
        Files.deleteIfExists(inFile);
        Files.createFile(inFile);

        try (FileOutputStream outStream = new FileOutputStream(inFile.toFile());
             BufferedOutputStream bufferedOut = new BufferedOutputStream(outStream);
             DataOutputStream dataStream = new DataOutputStream(bufferedOut)) {

            dataStream.writeInt(Integer.MIN_VALUE);

            for (int value = Integer.MIN_VALUE + 1; value != Integer.MIN_VALUE; ++value) {
                dataStream.writeInt(value);
            }
        }
    }

    /**
     * Use zero-copy to do the effective copy.
     * See: http://www.ibm.com/developerworks/library/j-zerocopy/index.html
     */
    public static void copy(Path src, Path dest) {

        checkArgument(src != null, "null 'src' passed");
        checkArgument(dest != null, "null 'dest' passed");

        try (FileChannel srcChannel = new FileInputStream(src.toFile()).getChannel();
             FileChannel destChannel = new FileOutputStream(dest.toFile()).getChannel()) {
            srcChannel.transferTo(0, srcChannel.size(), destChannel);
        }
        catch (IOException ioEx) {
            throw new IllegalStateException("Can't copy files", ioEx);
        }
    }

    public static void removeFilesWithExtension(Path dir, final String extension) {

        try {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(extension)) {
                        Files.delete(file);
                    }
                    return super.visitFile(file, attrs);
                }

            });
        }
        catch (IOException ioEx) {
            throw new IllegalStateException(ioEx);
        }
    }

    /**
     * Get file name without extension.
     *
     * @param filePath
     * @return
     * @throws IllegalArgumentException if 'filePath' isn't file
     */
    public static String[] getFileNameAndExtension(Path filePath) {
        if (!Files.isRegularFile(filePath)) {
            throw new IllegalArgumentException("'" + filePath + "' isn't a file");
        }

        String fullFileName = filePath.getFileName().toString();
        int extensionIndex = fullFileName.indexOf(".");

        if (extensionIndex < 0) {
            throw new IllegalArgumentException("'" + filePath + "' doesn't contain extension");
        }

        return new String[]{fullFileName.substring(0, extensionIndex), fullFileName.substring(extensionIndex + 1,
                                                                                              fullFileName.length())};
    }

    public static String getFileExtension(Path filePath) {
        if (!Files.isRegularFile(filePath)) {
            throw new IllegalArgumentException("'" + filePath + "' isn't a file");
        }

        String fileName = filePath.getFileName().toString();

        fileName = fileName.substring(fileName.indexOf(".") + 1, fileName.length());

        return fileName;
    }

    public static String toHex(byte[] data) {

        final int spacesCount = (data.length >>> 2) - 1;
        final StringBuilder buf = new StringBuilder(spacesCount + (data.length << 1));

        int j = 1;
        for (int i = 0; i < data.length; i++, j++) {

            buf.append(HEX[(data[i] >>> 4) & 0x0F]);
            buf.append(HEX[data[i] & 0x0F]);

            if (j < data.length && j % 4 == 0) {
                buf.append(" ");
            }
        }

        return buf.toString();

    }


    public static String createFileDigest(Path path) {

        List<String> words = new ArrayList<>();

        try {
            try (BufferedReader out = new BufferedReader(new FileReader(path.toFile()))) {
                String line = out.readLine();
                while (line != null) {

                    for (String word : line.split("\\s+")) {
                        words.add(word.toLowerCase().trim());
                    }

                    line = out.readLine();
                }
            }

            Collections.sort(words);

            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");

            for (String word : words) {
                digest.update(word.getBytes());
            }

            return toHex(digest.digest());
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }

    }


    public static void removeLineNumbers(Path inPath, Path outPath) {

        try {
            Files.deleteIfExists(outPath);
            Files.createFile(outPath);

            try (FileReader fileReader = new FileReader(inPath.toFile());
                 BufferedReader reader = new BufferedReader(fileReader);
                 FileWriter outWriter = new FileWriter(outPath.toFile());
                 BufferedWriter outBufWriter = new BufferedWriter(outWriter)) {

                String line = null;

                while ((line = reader.readLine()) != null) {
                    line = line.replaceFirst("\\d+", "") + System.getProperty("line.separator");
                    outBufWriter.write(line);
                }
            }
        }
        catch (FileNotFoundException fileNotFoundEx) {
            throw new IllegalArgumentException(fileNotFoundEx);
        }
        catch (IOException ioEx) {
            throw new IllegalStateException(ioEx);
        }
    }


    public static void deleteFile(File file) {
        if (!file.delete()) {
            throw new IllegalArgumentException("Can't delete file: " + file.getPath());
        }
    }

    public static Path createNew(String fullFilePath) {
        Path path = Paths.get(fullFilePath);
        createNew(path);
        return path;
    }


    public static void createNew(Path path) {
        try {
            if (Files.exists(path)) {
                Files.delete(path);
            }

            Files.createFile(path);
        }
        catch (final IOException ioEx) {
            throw new IllegalStateException(ioEx);
        }
    }

    public static void createNew(File file) {

        if (file.exists()) {
            deleteFile(file);
        }

        try {
            if (!file.createNewFile()) {
                throw new IllegalArgumentException("Can't generate new file, file already exists: " + file.getPath());
            }
        }
        catch (IOException ioEx) {
            throw new IllegalArgumentException("Can't generate new file: " + file.getPath(), ioEx);
        }
    }


    private FileUtils() {
        super();
    }

}
