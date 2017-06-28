package com.max.algs.epi.hashing;


import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Find a student with max average grade for top 3 exams.
 */
public final class StudentWithMaxKGrades {

    private static final Logger LOG = Logger.getLogger(StudentWithMaxKGrades.class);

    private static final int MAX_GRADE = 100;

    private static final String SPACE = " ";

    private StudentWithMaxKGrades() throws Exception {

        String[] ids = new String[10];
        for (int i = 0; i < ids.length; ++i) {
            ids[i] = "A" + (13 + i);
        }

        int k = 3;
        String[] data = generateData(ids);

        System.out.println(Arrays.toString(data));

        String bestId = findBestStudent(data, k).orElse("NOT_FOUND");

        System.out.printf("best id = %s %n", bestId);

        System.out.printf("LongestContainedInterval: java-%s %n", System.getProperty("java.version"));
    }

    /**
     * N - data.length
     * K - top 'k'
     * M - unique students count, can be up to N
     * <p>
     * time: O(N*lgK)
     * space: O(M*K)
     */
    public static Optional<String> findBestStudent(String[] data, int k) {
        checkNotNull(data);
        checkArgument(k > 0 && k < 1_000_000);

        if (data.length < k) {
            return Optional.empty();
        }

        Map<String, Queue<Integer>> mappedData = new HashMap<>();

        for (String line : data) {
            String[] lineData = line.split(SPACE);

            assert lineData.length == 2 : "incorrect data format";

            String id = lineData[0];
            int grade = Integer.valueOf(lineData[1]);

            Queue<Integer> minHeap =
                    mappedData.compute(id, (key, val) -> (val == null) ? new PriorityQueue<>(k) : val);

            if (minHeap.size() < k) {
                minHeap.add(grade);
            }
            else if (minHeap.peek() < grade) {
                int prevGrade = minHeap.remove();
                minHeap.add(grade);

                assert prevGrade < grade : " prevGrade >= grade";
            }

            assert minHeap.size() <= k : "minHeap is greater than 'k'";
        }

        double maxAvg = 0.0;
        String maxId = null;

        for (Map.Entry<String, Queue<Integer>> entry : mappedData.entrySet()) {

            Queue<Integer> grades = entry.getValue();

            assert grades.size() <= k : "'grades.size()' > 'k'";

            if (grades.size() == k) {

                double avg = grades.stream().mapToInt(val -> val).average().orElse(0.0);

                if (Double.compare(avg, maxAvg) > 0) {
                    maxAvg = avg;
                    maxId = entry.getKey();
                }
            }
        }

        return Optional.ofNullable(maxId);
    }

    private static String[] generateData(String[] ids) {

        Random rand = new Random();

        List<String> data = new ArrayList<>();

        for (String id : ids) {

            int examsCount = rand.nextInt(10);

            for (int i = 0; i < examsCount; ++i) {
                int grade = rand.nextInt(MAX_GRADE + 1);
                data.add(id + SPACE + grade);
            }
        }

        String[] arr = data.toArray(new String[0]);

        ArrayUtils.randomShuffle(arr);

        return arr;
    }

    public static void main(String[] args) {
        try {
            new StudentWithMaxKGrades();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}

