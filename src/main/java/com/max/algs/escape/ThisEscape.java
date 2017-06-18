package com.max.algs.escape;

import org.apache.log4j.Logger;

public class ThisEscape {

    private static final Logger LOG = Logger.getLogger(ThisEscape.class);

    private final int x;
    private final int y;
    private final Runnable runnnable;

    private ThisEscape(int otherX, int otherY) {

        this.runnnable = new Runnable() {
            @Override
            public void run() {
                if (x == 0) {
                    LOG.info("x = " + x + ", y = " + y);
                }
            }
        };

        this.x = otherX;
        this.y = otherY;
    }

    public static ThisEscape newInstance(int x, int y) {
        ThisEscape inst = new ThisEscape(x, y);
        new Thread(inst.runnnable).start();
        return inst;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
