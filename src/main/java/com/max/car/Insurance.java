package com.max.car;

import java.util.Date;
import java.util.Objects;

public final class Insurance {

    private final String name;
    private final Date expiration;

    public Insurance(String name, Date expiration) {
        this.name = Objects.requireNonNull(name);
        this.expiration = new Date(Objects.requireNonNull(expiration).getTime());
    }

    public String getName() {
        return name;
    }

    public Date getExpiration() {
        return new Date(expiration.getTime());
    }

    @Override
    public String toString() {
        return name + ", " + expiration;
    }
}
