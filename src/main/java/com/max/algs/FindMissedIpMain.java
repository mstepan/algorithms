package com.max.algs;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public final class FindMissedIpMain {

    private static final Logger LOG = Logger.getLogger(FindMissedIpMain.class);
    private static final Pattern IP_REGEXP = Pattern.compile("\\.");

    private FindMissedIpMain() throws Exception {

        Path filePath = Paths.get("/Users/mstepan/Desktop/ips.txt");

//        generateFileWithIps(filePath, "10.11.12.13", "133.211.0.0", "192.168.56.78");

        long startTime = System.currentTimeMillis();

        try (IpStream stream = new IpStream(filePath)) {
            String missedIp = findMissedIp(stream);
            LOG.info("Missed IP: " + missedIp);
        }

        long endTime = System.currentTimeMillis();

        // 7226341.0 ms ~ 2 hours
        System.out.printf("time: %d %nms", (endTime - startTime));

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * time: O(4*N)
     * space: O(256) ~ O(1)
     */
    public static String findMissedIp(IpStream data) {
        checkNotNull(data);

        int leftChunks = 4;
        String resPrefix = "";

        for (int i = 0; i < 4; ++i) {
            int[] counters = new int[256];

            while (data.hasNext()) {
                String ip = data.next().trim();

                if (ip.startsWith(resPrefix)) {
                    String[] parts = IP_REGEXP.split(ip);

                    assert i < parts.length : "Incorrect index for ip chunk: " + ip +
                            ", parts.length: " + parts.length;

                    int part = Integer.valueOf(parts[i]);

                    ++counters[part];
                }
            }

            int minIndex = 0;

            for (int j = 0; j < counters.length; ++j) {
                int cnt = counters[j];

                if (cnt == 0) {
                    // TODO: do optimization here, quick break. Use 'leftChunks' to determine left parts

                }

                if (cnt < counters[minIndex]) {
                    minIndex = j;
                }
            }

            resPrefix += (String.valueOf(minIndex) + ".");
            --leftChunks;

            LOG.info("Found so far: " + resPrefix);

            data.reset();
        }

        return resPrefix;
    }

    private static void generateFileWithIps(Path filePath, String... ipsToSkip) throws IOException {

        Set<String> skipSet = new HashSet<>();
        skipSet.addAll(Arrays.asList(ipsToSkip));

        Files.deleteIfExists(filePath);
        Files.createFile(filePath);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {

            for (int first = 0; first < 256; ++first) {
                for (int second = 0; second < 256; ++second) {
                    for (int third = 0; third < 256; ++third) {
                        for (int fourth = 0; fourth < 256; ++fourth) {

                            String ip = String.format("%s.%s.%s.%s", first, second, third, fourth);

                            if (skipSet.contains(ip)) {
                                LOG.info("Skipping ip: " + ip);
                            }
                            else {
                                writer.write(first + "." + second + "." + third + "." + fourth);
                                writer.newLine();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new FindMissedIpMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static final class IpStream implements AutoCloseable {

        final Path path;
        BufferedReader reader;
        String line;

        public IpStream(Path path) {
            this.path = path;

            try {
                this.reader = Files.newBufferedReader(path);
                line = reader.readLine();
            }
            catch (IOException ioEx) {
                LOG.error(ioEx.getMessage(), ioEx);
            }
        }

        public boolean hasNext() {
            return line != null;
        }

        public String next() {
            String res = line;
            try {
                line = reader.readLine();
            }
            catch (IOException ioEx) {
                LOG.error(ioEx.getMessage(), ioEx);
                line = null;
            }
            return res;
        }

        public void reset() {
            if (reader != null) {
                try {
                    reader.close();
                    reader = Files.newBufferedReader(path);
                    line = reader.readLine();
                }
                catch (IOException ioEx) {
                    LOG.error(ioEx.getMessage(), ioEx);
                }
            }
        }

        @Override
        public void close() {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                }
                catch (IOException ioEx) {
                    LOG.error(ioEx.getMessage(), ioEx);
                }
            }
        }
    }

}

