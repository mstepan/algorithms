package com.max.algs.concurrency;

import org.apache.log4j.Logger;

import java.util.concurrent.*;

public class CardsPrinterTask implements Runnable {

    public static final Integer MARKER = 1;
    private static final Logger LOG = Logger.getLogger(CardsPrinterTask.class);
    private static final String[] allCards = {"A", "K", "Q", "J", "10", "9", "8", "7", "6"};
    private final String cardName;
    private final CountDownLatch allFinished;
    private final BlockingQueue<Integer> inQueue;
    private final BlockingQueue<Integer> outQueue;
    private final boolean last;


    public CardsPrinterTask(String cardName, CountDownLatch allFinished, BlockingQueue<Integer> inQueue, BlockingQueue<Integer> outQueue, boolean last) {
        super();
        this.cardName = cardName;
        this.allFinished = allFinished;
        this.inQueue = inQueue;
        this.outQueue = outQueue;
        this.last = last;
    }

    public static void startAll() {
        try {
            final int threadsCount = 4;
            final CountDownLatch allPrintersFinished = new CountDownLatch(threadsCount);

            ExecutorService threadPool = Executors.newFixedThreadPool(threadsCount);

            String[] playingCards = {"spades", "clubs", "hearts", "diamonds"};

            BlockingQueue<Integer> firstQueue = new SynchronousQueue<Integer>();

            BlockingQueue<Integer> inQueue = firstQueue;
            BlockingQueue<Integer> outQueue = new SynchronousQueue<Integer>();

            for (int i = 0; i < threadsCount; i++) {

                threadPool.execute(new CardsPrinterTask(playingCards[i], allPrintersFinished, inQueue, outQueue, i == threadsCount - 1));

                inQueue = outQueue;

                if (i == threadsCount - 2) {
                    outQueue = firstQueue;
                }
                else {
                    outQueue = new SynchronousQueue<Integer>();
                }
            }

            firstQueue.put(CardsPrinterTask.MARKER);

            boolean reached = allPrintersFinished.await(5, TimeUnit.SECONDS);

            if (!reached) {
                LOG.error("Timeout exceeded");
            }

            threadPool.shutdown();

            LOG.info("Main finished");
        }
        catch (Exception ex) {
            LOG.error(ex);
        }
    }

    @Override
    public void run() {

        Thread.currentThread().setName(cardName + " thread");

        try {
            for (int i = 0; i < allCards.length && !Thread.currentThread().isInterrupted(); i++) {
                inQueue.take();
                LOG.info(allCards[i] + " " + cardName);
                if (i == allCards.length - 1 && last) {
                    break;
                }
                outQueue.put(MARKER);
            }
        }
        catch (InterruptedException interEx) {
            Thread.currentThread().interrupt();
            LOG.error(interEx);
        }
        finally {
            allFinished.countDown();
        }


    }


}
