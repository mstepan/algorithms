package com.max.algs;


import com.max.algs.ds.tree.geometry.XYLine;
import com.max.algs.ds.tree.geometry.XYPoint;

public final class AlgorithmsMain {


    private AlgorithmsMain() throws Exception {

        XYPoint[] points = {
                new XYPoint(1, 0),
                new XYPoint(3, 1),
                new XYPoint(6, 2),
                new XYPoint(9, 3),
        };

//        XYPoint[] points = {
//                new XYPoint(0, 0),
//                new XYPoint(1, 1),
//                new XYPoint(2, 2),
//                new XYPoint(3, 3),
//        };

        XYLine line = XYLine.bestFitLine(points);

        System.out.println(line);

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

