package com.max.car;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class CarUser {

    private final String firstName;
    private final String lastName;
    private final Optional<Car> car;

    public CarUser(String firstName, String lastName, Car car) {
        this.firstName = Objects.requireNonNull(firstName);
        this.lastName = Objects.requireNonNull(lastName);
        this.car = Optional.ofNullable(car);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Optional<Car> getCar() {
        return car;
    }

    public static String getInsuranceName(Optional<CarUser> userOptional) {
        return userOptional.
                flatMap(CarUser::getCar).
                flatMap(Car::getInsurance).
                map(Insurance::getName).
                orElse("<UNKNOWN>");


    }

    public static Optional<Insurance> findCheapest(Optional<CarUser> userOpt, Optional<Car> carOpt) {

        return userOpt.flatMap(u -> carOpt.map( c -> find(u, c)));
    }

    private static Insurance find(CarUser user, Car car) {
        return new Insurance("Insurance & Co.", Date.from(Instant.now().plusMillis(10000)));
    }

    public static Set<String> getUniqueInsuranceNames(List<CarUser> users) {
        return users.stream().
                map(CarUser::getCar).
                map(carOpt -> carOpt.flatMap(Car::getInsurance)).
                map(insOpt -> insOpt.map(Insurance::getName)).
//                flatMap( Optional::stream ).
        filter(Optional::isPresent).
                        map(Optional::get).
                        collect(Collectors.toSet());
    }
}
