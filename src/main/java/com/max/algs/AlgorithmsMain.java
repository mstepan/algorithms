package com.max.algs;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.DoubleBinaryOperator;

public final class AlgorithmsMain {

    public enum Operator {

        PLUS((x, y) -> x + y),
        SUB((x, y) -> x - y),
        MUL((x, y) -> x * y),
        DIV((x, y) -> x / y);

        private final DoubleBinaryOperator op;

        Operator(DoubleBinaryOperator op) {
            this.op = op;
        }

        public double apply(double left, double right) {
            return op.applyAsDouble(left, right);
        }
    }


    @FunctionalInterface
    interface RemoveEldestEntryFunction<K, V> {
        boolean removeEldestEntry(Map<K, V> map, Map.Entry<K, V> eldest);

        default boolean isValidKey(K key) {
            return "true".equals(key);
        }
    }

    private AlgorithmsMain() {

        Callable<Integer> c1 = () -> {
            System.out.println("Callable");
            return 177;
        };

        Runnable r1 = () -> System.out.println("Runnable");

        ExecutorService exec = Executors.newFixedThreadPool(10);
        try {

            exec.submit(c1);
            exec.submit(r1);
        }
        finally {
            exec.shutdownNow();
            try {
                exec.awaitTermination(1L, TimeUnit.SECONDS);
            }
            catch (InterruptedException interEx) {
                Thread.currentThread().interrupt();
            }
        }

//        BiPredicate<Map<String, Integer>, Map.Entry<String, Integer>> eldestEntryRemoverFunction =
//                (map, eldest) -> map.size() > 5;
//
//        final int maxElementsCount = 5;
//        final double defaultLoadFactor = 0.75;
//        final int capacity = (int) Math.round(maxElementsCount / defaultLoadFactor);
//
//        Map<String, Integer> map = new LinkedHashMap<String, Integer>(capacity, (float) defaultLoadFactor, true) {
//            @Override
//            protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
//                boolean willBeRemoved = size() > maxElementsCount;
//                if (willBeRemoved) {
//                    System.out.printf("removing %s %n", eldest);
//                }
//
//                return willBeRemoved;
//            }
//        };
//
//        map.put("one", 1);
//        map.put("two", 2);
//        map.put("three", 3);
//        map.put("four", 4);
//        map.put("five", 5);
//
//        map.get("one");
//        map.get("one");
//
//        map.get("two");
//
//        map.put("six", 6);
//
//        System.out.println(map);
    }

    private static boolean isEven(int value) {
        return (value & 1) == 0;
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
