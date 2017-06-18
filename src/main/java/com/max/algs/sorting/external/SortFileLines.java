package com.max.algs.sorting.external;

import com.max.algs.string.StringUtils;
import com.max.algs.util.FileUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class SortFileLines {

    private static final Logger LOG = Logger.getLogger(SortFileLines.class);

    private static final Path basePath = Paths
            .get("/Users/admin/repo/incubator/algorithms/src/main/java/com/max/algs/sorting/external");

    private static final Path inPath = Paths.get(basePath.toString(),
            "data.txt");

    private static final Path outPath = Paths.get(basePath.toString(),
            "data-sorted.txt");

    private static final Random RAND = ThreadLocalRandom.current();

    public SortFileLines() throws Exception {

        FileUtils.createNew(inPath);
        FileUtils.createNew(outPath);

        fillFileWithRandomLines(inPath);
        sortFile(inPath, outPath);

        LOG.info("File sorting done");

    }

    public static void main(String[] args) {
        try {
            new SortFileLines();
        }
        catch (Exception ex) {
            LOG.error(ex);
        }

    }

    void fillFileWithRandomLines(Path path) throws IOException {

        int minLength = 60;
        int avgLength = 120;

        try (BufferedWriter writer = Files.newBufferedWriter(path,
                Charset.defaultCharset())) {
            for (int i = 0; i < 100; i++) {

                int strLength = minLength
                        + RAND.nextInt(avgLength - minLength + 1);

                String str = StringUtils.randomLowerCase(strLength);
                writer.write(str);
                writer.newLine();
            }
        }
    }

    void sortFile(Path inPath, Path outPath) throws IOException {

        try (BufferedReader reader = Files.newBufferedReader(inPath,
                Charset.defaultCharset());
             BufferedWriter writer = Files.newBufferedWriter(outPath,
                     Charset.defaultCharset())) {

            // do actual sorting work here

        }

    }

}
