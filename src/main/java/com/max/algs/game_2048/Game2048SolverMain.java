package com.max.algs.game_2048;

/**
 * See: https://en.wikipedia.org/wiki/2048_(video_game)
 */
public final class Game2048SolverMain {


    private Game2048SolverMain() throws Exception {

        System.out.println("2048 solved");

        System.out.println("Main done...");
    }

    public static void main(String[] args) {
        try {
            new Game2048SolverMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
