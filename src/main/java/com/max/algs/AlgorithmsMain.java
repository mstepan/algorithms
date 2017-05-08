package com.max.algs;


public final class AlgorithmsMain {


    private AlgorithmsMain() throws Exception {

//        System.out.println(MathUtils(12, 16));

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

