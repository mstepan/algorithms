package com.max.algs.dto;

import com.google.auto.value.AutoValue;


@AutoValue
public abstract class Customer {

    public abstract String getName();

    public abstract int getAge();

    public abstract String getSsn();

    public static Builder builder(String name) {
        return new AutoValue_Customer.Builder().name(name);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder name(String name);
        public abstract Builder ssn(String ssn);
        public abstract Builder age(int age);
        public abstract Customer build();
    }

}
