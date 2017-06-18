package com.max.algs.dynamic;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Use dynamic programming to partition text with minimum cost.
 * <p>
 * N - words count
 * <p>
 * time: O(N^2)
 * space: O(N)
 */
public final class TextPartition {

    private static final Logger LOG = Logger.getLogger(TextPartition.class);

    private TextPartition() {
        throw new IllegalStateException("Can't instantiate utility class");
    }


    public static void printPartitionedString(String str, int rowSize) {
        for (String line : partition(str, rowSize)) {
            LOG.info(line + " [" + line.length() + "]");
        }
    }


    public static List<String> partition(String str, int rowSize) {

        if (str == null) {
            throw new IllegalArgumentException("Can't partition NULL string");
        }


        if (rowSize < 5) {
            throw new IllegalArgumentException("'rowSize' is very small, should be greater or equals to 5: " + rowSize);
        }

        if ("".equals(str.trim())) {
            return new ArrayList<>();
        }

        String[] words = str.split("\\s+");

        for (String singleWord : words) {
            if (singleWord.length() > rowSize) {
                throw new IllegalStateException("Word '" + singleWord + "' with size: " + singleWord.length() +
                        ", can't be fit with rowSize: " + rowSize);
            }
        }

        int[] cost = new int[words.length];
        int[] del = new int[words.length];

        cost[0] = rowSize - words[0].length();
        del[0] = 0;

        for (int j = 1; j < words.length; j++) {

            cost[j] = Integer.MAX_VALUE;
            int curSize = 0;

            for (int i = j; i >= 0; i--) {

                curSize += words[i].length();

                if (curSize > rowSize) {
                    break;
                }

                int curCost = rowSize - curSize + (i == 0 ? 0 : cost[i - 1]);

                if (curCost < cost[j]) {
                    cost[j] = curCost;
                    del[j] = i;
                }

                ++curSize;
            }

        }

        List<String> partitions = new ArrayList<>();

        int pos = del.length - 1;
        int partitionIndex = del[del.length - 1];

        List<String> curLine = new ArrayList<>();

        while (pos >= 0) {

            curLine.add(words[pos]);

            if (pos == partitionIndex) {
                partitionIndex = (pos == 0 ? -1 : del[pos - 1]);

                Collections.reverse(curLine);

                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < curLine.size(); i++) {
                    if (i != 0) {
                        buf.append(" ");
                    }

                    buf.append(curLine.get(i));
                }

                partitions.add(buf.toString());

                curLine.clear();
            }

            --pos;

        }

        Collections.reverse(partitions);

        return partitions;
    }

}
