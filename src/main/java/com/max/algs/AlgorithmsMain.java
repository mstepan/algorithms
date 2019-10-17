package com.max.algs;


import com.max.algs.cryptography.DiffieHelmanKeyPart;

import java.math.BigInteger;
import java.util.Random;

public final class AlgorithmsMain {


    private AlgorithmsMain() {
//
//        Insurance insurance1 = new Insurance("Insurance & Co.", Date.from(Instant.now().plusMillis(10000)));
//
//        Insurance insurance2 = new Insurance("Best insurance", Date.from(Instant.now().plusMillis(10000)));
//
//        Car car = new Car("Audi", 2017, insurance1);
//        Car bmw = new Car("BMW", 2017, insurance2);
//
//        CarUser user1 = new CarUser("Maksym", "Stepanenko", car);
//
//        CarUser user2 = new CarUser("Maksym", "Stepanenko", null);
//
//        CarUser user3 = new CarUser("Maksym", "Stepanenko", bmw);

//        System.out.println(CarUser.getUniqueInsuranceNames(Arrays.asList(user1, user2, user3)));

        final int g = 133;
        final int p = 7879;

        DiffieHelmanKeyPart a = new DiffieHelmanKeyPart(g, p);
        BigInteger aPart = a.getPart();

        DiffieHelmanKeyPart b = new DiffieHelmanKeyPart(g, p);
        BigInteger bPart = b.getPart();

        BigInteger key1 = a.constructKey(bPart);

        BigInteger key2 = b.constructKey(aPart);

        System.out.printf("key1: %d, key2: %d %n", key1, key2);


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
