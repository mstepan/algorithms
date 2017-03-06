package com.max.backoff;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Thread safe exponential back off.
 * <p>
 * see: https://en.wikipedia.org/wiki/Exponential_backoff
 */
public final class ExponentialBackoff implements AutoCloseable {

    private static final Random RAND = ThreadLocalRandom.current();

    private static final ThreadLocal<Integer> ATTEMPTS_COUNT = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    private final long delay;
    private final long maxDelay;
    private final TimeUnit unit;

    public ExponentialBackoff(long delay, long maxDelay, TimeUnit unit) {
        checkArgument(delay >= 0, "negative 'delay' parameter passed %s", delay);
        checkArgument(maxDelay >= 0, "negative 'maxDelay' parameter passed %s", delay);
        checkArgument(maxDelay >= delay, "'maxDelay' < 'delay': %s < %s", maxDelay, delay);
        checkArgument(unit != null, "'unit' parameter is null");
        this.delay = delay;
        this.maxDelay = maxDelay;
        this.unit = unit;
    }

    public void backoff() throws InterruptedException {

        int attemptsSnapshot = ATTEMPTS_COUNT.get();

        int coefficient = RAND.nextInt(2 << attemptsSnapshot);

        // sleep = max( maxDelay, delay * rand[0; 2^attempts-1])
        unit.sleep(Math.min(maxDelay, delay * coefficient));

        ATTEMPTS_COUNT.set(attemptsSnapshot + 1);
    }

    @Override
    public void close() throws Exception {
        ATTEMPTS_COUNT.remove();
    }
}
