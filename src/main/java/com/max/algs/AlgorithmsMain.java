package com.max.algs;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class AlgorithmsMain {

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

