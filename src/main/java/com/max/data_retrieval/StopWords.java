package com.max.data_retrieval;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public enum StopWords {

    INST;

    private final Set<String> stopWordsDic = new HashSet<>();

    private StopWords() {
        Path path =
                Paths.get("/Users/mstepan/repo/incubator/algorithms/src/main/java/com/max/data_retrieval/stop_words_en.txt");

        try (BufferedReader reader = Files.newBufferedReader(path)) {

            String singleStopWord = reader.readLine();

            stopWordsDic.add(singleStopWord);

        }
        catch (IOException ioEx) {
            throw new IllegalStateException("Can't read stop words file from: " + path);
        }
    }


    public boolean isStopWord(String str) {
        return stopWordsDic.contains(str) || str.length() < 3;
    }
}
