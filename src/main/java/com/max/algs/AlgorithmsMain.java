package com.max.algs;


import org.apache.log4j.Logger;

import java.util.PriorityQueue;
import java.util.Queue;

public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);

    private AlgorithmsMain() {

        Queue<User> q = new PriorityQueue<>();

        q.remove(new User());

        LOG.info("Main done: java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static final class User {

    }
}

