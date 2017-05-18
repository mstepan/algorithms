package com.max.algs;


import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public final class AlgorithmsMain {

    private AlgorithmsMain() throws Exception {

        List<Integer> data = stream(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}).
                map(val -> val * val).
                filter(val -> (val & 1) == 0).
                sorted().
                boxed().
                collect(Collectors.toList());

        System.out.println(data);

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

