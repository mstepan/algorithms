package com.max.algs.hashing.perfect;

import com.max.algs.util.ArrayUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PerfectHashSetTest {

    private static final Logger LOG = Logger.getLogger(PerfectHashSetTest.class);

    private static final Random RANDOM = ThreadLocalRandom.current();


    @Test
    public void contains() {

        final int ITERATIONS_COUNT = 100;
        final int ELEMS_COUNT = 1000;
        final int NOT_EXISTED_ELEMS_IT_COUNT = 1000;

        for (int i = 0; i < ITERATIONS_COUNT; i++) {

            Set<Integer> values = new HashSet<>();

            int[] arr = ArrayUtils.generateRandomArray(ELEMS_COUNT);

            for (int value : arr) {
                values.add(value);
            }

            PerfectHashSet<Integer> table = new PerfectHashSet<Integer>(values /*, new UniversalHashFunction<Integer>(397311102, 323875702), buckets*/);

            for (int value : arr) {
                assertTrue("Can't find value: " + value, table.contains(value));
            }

            for (int j = 0; j < NOT_EXISTED_ELEMS_IT_COUNT; j++) {

                int randValue = RANDOM.nextInt();

                while (values.contains(randValue)) {
                    LOG.info("Chhosing another rand value");
                    randValue = RANDOM.nextInt();
                }

                assertFalse(table.contains(randValue));
            }

        }
    }


}
