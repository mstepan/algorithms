package com.max.algs;


import rx.Observable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class AlgorithmsMain {

    private static void iteratorApproach() {
        List<String> data = Arrays.asList("One", "Two", "Three", "Four", "Five");

        Iterator<String> it = data.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    private static void reactiveApproach() {
        List<String> data = Arrays.asList("One", "Two", "Three", "Four", "Five");

        Observable<String> lazyData = Observable.from(data);

        lazyData.subscribe(System.out::println,
                           Throwable::printStackTrace,
                           () -> System.out.println("All done"));
    }

    private AlgorithmsMain() throws Exception {

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

