package com.max.algs.presorted;


public class OpenInterval {

    private final double a;
    private final double b;


    public OpenInterval(double a, double b) {
        this.a = a;
        this.b = b;
    }


    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }


    public OpenInterval overlap(OpenInterval other) {
        if (other == null) {
            throw new IllegalArgumentException("'other' parameter is NULL");
        }

        OpenInterval left = other;
        OpenInterval right = this;

        // exchange intervals left <-> right
        if (right.a < left.a) {
            left = this;
            right = other;
        }

        // intervals do not overlap
        if (left.b <= right.a) {
            return null;
        }

        // one interval within another, right within left
        if (right.b <= left.b) {
            return new OpenInterval(right.a, right.b);
        }

        return new OpenInterval(right.a, left.b);
    }


    @Override
    public String toString() {
        return "(" + a + ", " + b + ")";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OpenInterval xyPoint = (OpenInterval) o;

        if (Double.compare(xyPoint.a, a) != 0) {
            return false;
        }
        if (Double.compare(xyPoint.b, b) != 0) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = a != +0.0d ? Double.doubleToLongBits(a) : 0L;
        result = (int) (temp ^ (temp >>> 32));
        temp = b != +0.0d ? Double.doubleToLongBits(b) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
