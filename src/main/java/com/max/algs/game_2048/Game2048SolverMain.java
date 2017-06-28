package com.max.algs.game_2048;

import org.apache.log4j.Logger;

/**
 * See: https://en.wikipedia.org/wiki/2048_(video_game)
 */
public final class Game2048SolverMain {

    private static final Logger LOG = Logger.getLogger(Game2048SolverMain.class);

    private Game2048SolverMain() throws Exception {

        System.out.println("2048 solved");

        System.out.println("Main done...");
    }

    public static void main(String[] args) {
        try {
            new Game2048SolverMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }


}
