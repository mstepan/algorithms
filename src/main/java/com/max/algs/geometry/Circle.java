package com.max.algs.geometry;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Circle representation in 2d coordinates.
 */
public final class Circle {

    private final int x;
    private final int y;
    private final int radius;
    private int hash;

    private Circle(CircleBuilder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.radius = builder.radius;
        checkArgument(this.radius > 0, "Negative or zero radius passed '%s'", this.radius);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public boolean isIntersected(Circle other) {

        checkNotNull(other);

        int dx = x - other.x;
        int dy = y - other.y;

        int distance = dx * dx + dy * dy;

        int r1 = radius;
        int r2 = other.radius;

        int radiusSumSquare = r1 * r1 + 2 * r1 * r2 + r2 * r2;

        return distance <= radiusSumSquare;
    }

    @Override
    public int hashCode() {

        if (hash == 0) {
            int res = 17;

            res = 31 * res + x;
            res = 31 * res + y;
            res = 31 * res + radius;

            hash = res;
        }

        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        Circle other = (Circle) obj;

        return hash == other.hash &&
                x == other.x &&
                y == other.y &&
                radius == other.radius;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", r = " + radius + ")";
    }

    public static final class CircleBuilder {
        final int x;
        final int y;
        int radius;

        public CircleBuilder(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public CircleBuilder radius(int radius) {
            this.radius = radius;
            return this;
        }

        public Circle build() {
            return new Circle(this);
        }
    }

}
