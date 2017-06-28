package com.max.data_retrieval;


import com.google.common.base.Joiner;
import com.max.algs.fulltextsearch.SnowballStemmer;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class MainIndexer {

    private static final Logger LOG = Logger.getLogger(MainIndexer.class);

    private static final Joiner JOINER = Joiner.on(" ");

    private MainIndexer() throws Exception {

        int lineNumber = 1;

        final Map<String, Set<Integer>> invertedIndex = new TreeMap<>();

        Path inPath =
                Paths.get("/Users/mstepan/repo/incubator/algorithms/src/main/java/com/max/data_retrieval/in.txt");

        try (BufferedReader reader = Files.newBufferedReader(inPath)) {
            String line;

            while ((line = reader.readLine()) != null) {

                String normalLine = LineNormaliser.INST.normalizeLine(line).trim();

                if (normalLine.length() == 0) {
                    continue;
                }

                String[] tokens = normalLine.split("\\s+");

                for (String token : tokens) {

                    String stemmedToken = SnowballStemmer.INST.stemm(token).trim();

                    if (isDigit(token) || StopWords.INST.isStopWord(token)) {
                        continue;
                    }

                    Set<Integer> postings = invertedIndex.get(stemmedToken);

                    if (postings == null) {
                        postings = new TreeSet<>();
                        invertedIndex.put(stemmedToken, postings);
                    }

                    postings.add(lineNumber);

                }

                ++lineNumber;

//                System.out.println(JOINER.join(tokens));

            }
        }

        List<Integer> queryResult = query(invertedIndex, new String[]{"with", "online"});

        System.out.println("query result: " + queryResult);

        Path invertedFilePath =
                Paths.get("/Users/mstepan/repo/incubator/algorithms/src/main/java/com/max/data_retrieval/index.txt");

        Files.deleteIfExists(invertedFilePath);
        Files.createFile(invertedFilePath);

        try (BufferedWriter out = Files.newBufferedWriter(invertedFilePath)) {
            for (Map.Entry<String, Set<Integer>> entry : invertedIndex.entrySet()) {

                StringBuilder buf = new StringBuilder(entry.getKey());
                buf.append("=>");

                for (Integer singlePosting : entry.getValue()) {
                    buf.append(singlePosting).append(", ");
                }

                out.write(buf.toString());
                out.newLine();
            }
        }

        System.out.println("MainIndexer done");

    }

    public static void main(String[] args) {
        try {
            new MainIndexer();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }

    }

    private boolean isDigit(String str) {

        if (str.endsWith("th") || str.endsWith("st") || str.endsWith("nd") || str.endsWith("rd")) {
            str = str.substring(0, str.length() - 2);
        }
        else if (str.endsWith("d")) {
            str = str.substring(0, str.length() - 1);
        }

        for (int i = 0, length = str.length(); i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private List<Integer> query(Map<String, Set<Integer>> invertedIndex, String[] query) {

        List<Integer> queryResult = new ArrayList<>();

        int index = 0;

        while (index < query.length) {

            String queryParameter = query[index];

            if (invertedIndex.containsKey(queryParameter)) {
                queryResult.addAll(invertedIndex.get(queryParameter));
                break;
            }

            ++index;
        }

        ++index;

        for (int i = index; i < query.length; i++) {

            String queryParameter = query[index];

            Set<Integer> singleTermPostingList =
                    (invertedIndex.get(queryParameter) == null ? new HashSet<>() :
                            invertedIndex.get(queryParameter));

            queryResult = intersect(queryResult, singleTermPostingList);

        }


        return queryResult;
    }

    private List<Integer> intersect(Collection<Integer> first, Collection<Integer> second) {

        List<Integer> res = new ArrayList<>();

        Iterator<Integer> it1 = first.iterator();
        Iterator<Integer> it2 = second.iterator();

        Integer doc1 = nextOrNull(it1);
        Integer doc2 = nextOrNull(it2);

        while (doc1 != null && doc2 != null) {

            if (doc1.compareTo(doc2) == 0) {
                res.add(doc1);
                doc1 = nextOrNull(it1);
                doc2 = nextOrNull(it2);
            }
            else if (doc1.compareTo(doc2) > 0) {
                doc2 = nextOrNull(it2);
            }
            else {
                doc1 = nextOrNull(it1);
            }

        }

        return res;
    }

    private Integer nextOrNull(Iterator<Integer> it) {
        return it.hasNext() ? it.next() : null;
    }

}
