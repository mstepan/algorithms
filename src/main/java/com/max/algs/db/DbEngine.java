package com.max.algs.db;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * DB engine. Append only log.
 */
public final class DbEngine {

    private static final Logger LOG = Logger.getLogger(DbEngine.class);

    private static final Path DB_PATH = Paths.get
            ("/Users/mstepan/repo/incubator/algorithms/src/main/java/com/max/algs/db/database.txt");

    private final BufferedWriter file;

    public DbEngine() {
        file = createFile(DB_PATH);
        LOG.info("DbEngine started from file: '" + DB_PATH + "'");
    }

    public static void main(String[] args) {
        try {
            Pattern pattern = Pattern.compile("^set\\s([\\d\\w]+)\\s([}{\\]\\[\\w\\d\"\'\\s:_,]+)$");

            DbEngine dbEngine = new DbEngine();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

                while (true) {

                    String command = reader.readLine();

                    if ("exit".equals(command)) {
                        break;
                    }

                    if (command.startsWith("get")) {
                        String value = dbEngine.get(command.split("\\s+")[1]);

                        LOG.info("found value: " + value);
                    }
                    else if (command.startsWith("set")) {

                        Matcher matcher = pattern.matcher(command);

                        if (matcher.matches()) {
                            String key = matcher.group(1);
                            String value = matcher.group(2);

                            System.out.println(key + " = " + value);

                            dbEngine.set(key, value);
                            System.out.println("value set");
                        }

                    }
                    else {
                        throw new IllegalArgumentException("Incorrect command detected: '" + command + "'");
                    }
                }
            }

            LOG.info("DBEngine stopped...");
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private BufferedWriter createFile(Path path) {
        try {
            return new BufferedWriter(new FileWriter(path.toFile(), true));
        }
        catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * time: O(N)
     */
    public String get(String key) {

        String lastFoundValue = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(DB_PATH.toFile()))) {

            String line = reader.readLine();

            while (line != null) {

                if (line.startsWith(key)) {
                    String[] values = line.split("=");
                    lastFoundValue = values[1];
                }

                line = reader.readLine();
            }

        }
        catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        }

        return lastFoundValue;
    }

    /**
     * time: O(1)
     */
    public void set(String key, String value) {
        try {
            file.write(key + "=" + value);
            file.newLine();
            file.flush();
        }
        catch (IOException ioEx) {
            LOG.error(ioEx.getMessage(), ioEx);
        }
    }


}
