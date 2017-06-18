package com.max.algs.fulltextsearch;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkArgument;

public class ReutersDataCleaner {


    private static final Logger LOG = Logger.getLogger(ReutersDataCleaner.class);

    private ReutersDataCleaner() throws Exception {
        Path baseDir = Paths.get("/Users/admin/repo/incubator/data/reuters");
        Path destDir = Paths.get(baseDir.getParent().toString(), "reuters-cleaned");

        checkArgument(Files.exists(baseDir), "Can't find base dir %s", baseDir);

        Files.list(destDir).forEach(path -> {
            try {
                Files.deleteIfExists(path);
            }
            catch (IOException ioEx) {
                LOG.error("Can't delete file", ioEx);
            }
        });

        Files.deleteIfExists(destDir);
        Files.createDirectory(destDir);

        Files.list(baseDir).forEach(new FileCleaner(destDir));

        LOG.info("ReutersDataCleaner completed");
    }

    public static void main(String[] args) {

        try {
            new ReutersDataCleaner();
        }
        catch (Exception ex) {
            LOG.error("Error occurred", ex);
        }

    }

    private static class FileCleaner implements Consumer<Path> {

        private static final String START_TAG = "<BODY>";
        private static final String END_TAG = "&#3;</BODY>";

        private final Path destDir;

        private int documentsCount;

        FileCleaner(Path destDir) {
            this.destDir = destDir;
        }


        @Override
        public void accept(Path path) {

            try {

                try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("latin1"))) {

                    String line = null;

                    while ((line = reader.readLine()) != null) {

                        int startTagIndex = line.indexOf(START_TAG);

                        if (startTagIndex != -1) {
                            ++documentsCount;

                            Path destFile = Paths.get(destDir.toString(), documentsCount + "-" + path.getFileName().toString());
                            Files.createFile(destFile);

                            try (BufferedWriter writer = Files.newBufferedWriter(destFile)) {
                                writer.write(line.substring(startTagIndex + START_TAG.length()));
                                writer.newLine();

                                while ((line = reader.readLine()) != null) {

                                    int endTagIndex = line.indexOf(END_TAG);

                                    if (endTagIndex != -1) {
                                        writer.write(line.substring(0, endTagIndex));
                                        break;
                                    }

                                    writer.write(line);
                                    writer.newLine();
                                }
                            }

                        }
                    }

                }
            }
            catch (IOException ioEx) {
                LOG.error("Can't read file: '" + path.toString() + "'", ioEx);
            }
        }

    }

}
