package com.max.algs;


import com.max.algs.ds.Pair;
import org.apache.log4j.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);


    static class Activity {
        private ActivityDetails details;

        Activity(ActivityDetails details) {
            this.details = details;
        }

        public ActivityDetails getDetails() {
            return details;
        }
    }

    static class ActivityDetails {
        private User user;

        ActivityDetails(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }
    }

    static class User {
        final String name;

        User(String name) {
            this.name = name;
        }
    }


    private AlgorithmsMain() throws Exception {

//        Activity activity = new Activity(new ActivityDetails(new User("max")));
        Activity activity = new Activity(new ActivityDetails(null));

        if ("max".equals(activity.getDetails().getUser().name)) {
            LOG.info("It's max");
        }
        else {
            LOG.info("Unknown user name");
        }

        LOG.info("Main done... java-" + System.getProperty("java.version"));
    }

    /**
     * Use (3/2) * N = 1.5 * N comparisons to find min and max instead of standard one 2 * N comparisons.
     */
    private static Pair<Integer, Integer> findMinMax(int[] arr) {

        // side effect here
        checkNotNull(arr);

        if (arr.length == 0) {
            return Pair.empty();
        }

        int minVal = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;

        for (int i = 1; i < arr.length; ++i) {

            int curMin = arr[i - 1];
            int curMax = arr[i];

            if (curMin > curMax) {
                int temp = curMin;
                curMin = curMax;
                curMax = temp;
            }

            if (curMin < minVal) {
                minVal = curMin;
            }

            if (curMax > maxValue) {
                maxValue = curMax;
            }
        }

        // handle odd length array
        if ((arr.length & 1) != 0) {
            int last = arr[arr.length - 1];

            if (last < minVal) {
                minVal = last;
            }

            if (last > maxValue) {
                maxValue = last;
            }
        }

        return Pair.of(minVal, maxValue);
    }

    public static void main(String[] args) {
        try {

            new AlgorithmsMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
