package com.max.algs.concurrency;

import org.apache.log4j.Logger;
import org.jmock.lib.concurrent.Blitzer;
import org.junit.Test;

import java.lang.invoke.MethodHandles;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.Assert.*;


public class SimpleBlockingQueueTest {

    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    private static final int NUM_OF_ITERATIONS = 1000;

    private static final class UuidProducer implements Runnable {

        final SimpleBlockingQueue<UUID> queue;
        final ConcurrentMap<UUID, UUID> map;

        UuidProducer(SimpleBlockingQueue<UUID> queue, ConcurrentMap<UUID, UUID> map) {
            this.queue = queue;
            this.map = map;
        }

        @Override
        public void run() {
            for (int i = 0; i < NUM_OF_ITERATIONS; ++i) {
                UUID randomValue = UUID.randomUUID();
                map.put(randomValue, randomValue);
                queue.add(randomValue);
                Thread.yield();
            }
        }
    }

    private static final class UuidConsumer implements Callable<Boolean> {

        final SimpleBlockingQueue<UUID> queue;
        final ConcurrentMap<UUID, UUID> map;
        final CountDownLatch allConsumersDone;
        boolean hasErrors;

        UuidConsumer(SimpleBlockingQueue<UUID> queue, ConcurrentMap<UUID, UUID> map,
                     CountDownLatch allConsumersDone) {
            this.queue = queue;
            this.map = map;
            this.allConsumersDone = allConsumersDone;
        }

        @Override
        public Boolean call() {
            try {
                for (int i = 0; i < NUM_OF_ITERATIONS; ++i) {
                    UUID value = queue.poll();
                    if (!map.remove(value).equals(value)) {
                        LOG.info("Error");
                        hasErrors = true;
                    }
                    Thread.yield();
                }
            }
            catch (InterruptedException interEx) {
                Thread.currentThread().interrupt();
            }
            finally {
                allConsumersDone.countDown();
            }

            return hasErrors;
        }
    }

    @Test
    public void multipleProducersConsumers() throws Exception {

        final int numOfConsumers = 20;

        SimpleBlockingQueue<UUID> queue = new SimpleBlockingQueue<>();
        ConcurrentMap<UUID, UUID> map = new ConcurrentHashMap<>();

        CountDownLatch allConsumersDone = new CountDownLatch(numOfConsumers);
        ExecutorService pool = Executors.newCachedThreadPool();

        @SuppressWarnings("unchecked")
        Future<Boolean>[] consumersResults = new Future[numOfConsumers];

        for (int i = 0; i < numOfConsumers; ++i) {
            pool.execute(new UuidProducer(queue, map));
            consumersResults[i] = pool.submit(new UuidConsumer(queue, map, allConsumersDone));
        }

        allConsumersDone.await();

        for (Future<Boolean> singleConsumerRes : consumersResults) {
            assertFalse(singleConsumerRes.get());
        }

        assertTrue(map.isEmpty());
    }


    @Test
    public void stressTest() throws Exception {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        Blitzer blitzer = new Blitzer(10_000, 10);
        blitzer.blitz(250, () -> {
            queue.add(rand.nextInt());
            try {
                queue.poll();
            }
            catch (InterruptedException ex) {
            }
        });

        assertEquals(0, queue.size());
    }
}
