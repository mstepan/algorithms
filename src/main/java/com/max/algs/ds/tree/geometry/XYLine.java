package com.max.algs.ds.tree.geometry;

import com.google.common.base.Objects;

/**
 * Y = a * X + b
 */
public final class XYLine {

    private final double a;
    private final double b;

    public XYLine(double a, double b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Construct best fit line using array of points.
     */
    public static XYLine bestFitLine(XYPoint[] points) {

        int n = points.length;

        double xy = 0.0;
        double xi = 0.0;
        double yi = 0.0;

        double xi2 = 0.0;

        for (XYPoint point : points) {
            xy += point.x * point.y;
            xi += point.x;
            yi += point.y;
            xi2 += (point.x * point.x);
        }

        double a = (n * xy - xi * yi) / (n * xi2 - xi * xi);
        double b = (yi - a * xi) / n;

        return new XYLine(a, b);
    }

    public double evaluate(double x) {
        return a * x + b;
    }

    public double calculateError(XYPoint[] points) {
        double error = 0.0;

        for (XYPoint point : points) {
            double diff = evaluate(point.x) - point.y;
            error += (diff * diff);
        }
        return error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        XYLine xyLine = (XYLine) o;
        return Double.compare(xyLine.a, a) == 0 &&
                Double.compare(xyLine.b, b) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(a, b);
    }

    @Override
    public String toString() {

        if (Double.compare(b, 0.0) == 0) {
            return String.format("y = %s * x", a);
        }

        return String.format("y = %s * x %s %s", a, (Math.signum(b) == -1 ? "-" : "+"), Math.abs(b));
    }
}
