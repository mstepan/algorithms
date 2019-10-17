package com.max.algs.cryptography;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class DiffieHelmanKeyPart {

    private static final Random RAND = ThreadLocalRandom.current();

    private final BigInteger g;
    private final BigInteger p;
    private final BigInteger a;

    public DiffieHelmanKeyPart(int g, int p) {
        this.g = BigInteger.valueOf(g);
        this.p = BigInteger.valueOf(p);
        this.a = BigInteger.valueOf(RAND.nextInt(10));
    }

    public BigInteger getPart() {
        return g.modPow(a, p);
    }

    public BigInteger constructKey(BigInteger otherPart) {
        return otherPart.modPow(a, p);
    }
}
