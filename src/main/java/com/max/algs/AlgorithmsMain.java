package com.max.algs;


public final class AlgorithmsMain {


    private AlgorithmsMain() throws Exception {

        String node1 = "";
        String node2 = null;

        boolean res = node1 != null || node2 != null;

        System.out.println(res);

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

