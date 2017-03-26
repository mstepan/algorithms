package com.max.algs;


import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;
import rx.subscriptions.Subscriptions;

import java.util.concurrent.TimeUnit;

public final class AlgorithmsMain {

    private static final class TestSubscriber<T> extends Subscriber<T> {

        @Override
        public void onCompleted() {
            System.out.println("Completed");
        }

        @Override
        public void onError(Throwable ex) {
            System.out.println("Error: " + ex.getMessage());
            unsubscribe();
        }

        @Override
        public void onNext(T value) {
            System.out.println(Thread.currentThread().getName() + ": " + value);
        }
    }

    private static final class TwitterStream1 {

        private final Subject<String, String> subject = ReplaySubject.create();

        public static TwitterStream1 create(String username, String password) {

            TwitterStream1 obs = new TwitterStream1();

            Thread th = new Thread(() -> {

                int cnt = 0;

                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        obs.subject.onNext("cnt: " + cnt);
                        ++cnt;
                        TimeUnit.SECONDS.sleep(1);
                    }
                    catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            th.setDaemon(true);
            th.start();

            return obs;

        }

        public Observable<String> observable() {
            return subject;
        }
    }

    private AlgorithmsMain() throws Exception {

        Observable<String> twitterObs = Observable.create(subs -> {

            Thread th = new Thread(() -> {

                System.out.println("Connecting to network...");

                int cnt = 0;

                while (!subs.isUnsubscribed() && !Thread.currentThread().isInterrupted()) {
                    try {
                        subs.onNext("cnt: " + cnt);
                        ++cnt;
                        TimeUnit.SECONDS.sleep(1);
                    }
                    catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        subs.onError(ex);
                    }
                }

                subs.onCompleted();

                System.out.println(Thread.currentThread().getName() + " completed...");
            });

            subs.add(Subscriptions.create(() -> {
                System.out.println("Disconnect");
                subs.unsubscribe();
            }));

            th.start();
        });

        Observable<String> twitterObsLazy = twitterObs.publish().refCount();
        TimeUnit.SECONDS.sleep(3);

        Subscription subscription1 = twitterObsLazy.subscribe(new TestSubscriber<>());
        TimeUnit.SECONDS.sleep(3);

        Subscription subscription2 = twitterObsLazy.subscribe(new TestSubscriber<>());

        TimeUnit.SECONDS.sleep(5);
        subscription1.unsubscribe();

        TimeUnit.SECONDS.sleep(5);
        subscription2.unsubscribe();

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}

