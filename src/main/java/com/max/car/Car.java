package com.max.car;

import java.util.Objects;
import java.util.Optional;


public final class Car {

    private final String model;
    private final int year;
    private final Optional<Insurance> insurance;

    public Car(String model, int year, Insurance insurance) {
        this.model = Objects.requireNonNull(model);
        this.year = Objects.requireNonNull(year);
        this.insurance = Optional.ofNullable(insurance);
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public Optional<Insurance> getInsurance() {
        return insurance;
    }
}
