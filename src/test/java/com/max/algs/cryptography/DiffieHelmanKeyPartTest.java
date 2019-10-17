package com.max.algs.cryptography;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class DiffieHelmanKeyPartTest {

    @Test
    public void checkSecretKeysAreTheSame() {

        final Random rand = new Random();

        int[] primes = new int[]{7879, 12967, 12973, 12979, 12983, 13001, 13003, 13007, 13009, 13033, 13037,
                13043, 13049, 13063, 13093, 13099, 13103, 13109, 13121, 13127, 13147,
                13151, 13159, 13163, 13171, 13177, 13183, 13187, 13217, 13219, 13229,
                13241, 13249, 13259, 13267, 13291, 13297, 13309, 13313, 13327, 13331,
                13337};

        for (int p : primes) {
            final int g = 100 + rand.nextInt(100);

            DiffieHelmanKeyPart a = new DiffieHelmanKeyPart(g, p);
            BigInteger aPart = a.getPart();

            DiffieHelmanKeyPart b = new DiffieHelmanKeyPart(g, p);
            BigInteger bPart = b.getPart();

            BigInteger key1 = a.constructKey(bPart);

            BigInteger key2 = b.constructKey(aPart);

            assertThat(key1).isEqualTo(key2);
        }


    }
}
