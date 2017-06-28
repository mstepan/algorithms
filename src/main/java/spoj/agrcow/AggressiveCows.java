package spoj.agrcow;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * http://www.spoj.com/problems/AGGRCOW/
 */
public class AggressiveCows {

    private static final Logger LOG = Logger.getLogger(AggressiveCows.class);

    private AggressiveCows() throws Exception {

        String filePath = "/Users/mstepan/repo/algorithms/src/main/java/spoj/agrcow/in.txt";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {

//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            int testCases = Integer.parseInt(reader.readLine());

            for (int i = 0; i < testCases; ++i) {
                String[] values = reader.readLine().split("\\s+");

                int n = Integer.parseInt(values[0]);
                int cows = Integer.parseInt(values[1]);

                int[] stalls = new int[n];

                for (int j = 0; j < stalls.length; ++j) {
                    stalls[j] = Integer.parseInt(reader.readLine());
                }

                Arrays.sort(stalls);

                System.out.println(calculateOptimalPlacement(stalls, cows));
            }
        }
    }


    private static int calculateOptimalPlacement(int[] stalls, int cows) {

        int lo = 1;
        int hi = sum(stalls);
        int optimal = -1;

        while (lo <= hi) {
            int mid = lo + ((hi - lo) >> 1);

            if (canPlaceAtDistance(stalls, cows, mid)) {
                optimal = mid;
                lo = mid + 1;
            }
            else {
                hi = mid - 1;
            }
        }

        return optimal;
    }

    private static boolean canPlaceAtDistance(int[] stalls, int cows, int reqDistance) {
        int cowsLeft = cows - 1;
        int last = stalls[0];

        for (int i = 1; i < stalls.length && cowsLeft > 0; ++i) {
            if (stalls[i] - last >= reqDistance) {
                last = stalls[i];
                --cowsLeft;
            }
        }

        return cowsLeft == 0;
    }

    private static int sum(int[] arr) {
        int res = 0;
        for (int val : arr) {
            res += val;
        }
        return res;
    }

    public static void main(String[] args) {
        try {
            new AggressiveCows();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
