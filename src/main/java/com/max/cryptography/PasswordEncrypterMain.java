package com.max.cryptography;


import org.apache.log4j.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import static com.max.algs.util.FileUtils.toHex;

public final class PasswordEncrypterMain {

    private static final Logger LOG = Logger.getLogger(PasswordEncrypterMain.class);


    private PasswordEncrypterMain() {

        long startTime = System.currentTimeMillis();

        byte[] data = pbkdf2("Password1!", new byte[32], 120_000, 512);

        long endTime = System.currentTimeMillis();

        LOG.info(toHex(data));

        LOG.info("time: " + (endTime - startTime));

        LOG.info("AesMain done... java-" + System.getProperty("java.version"));
    }

    private static byte[] pbkdf2(String password, byte[] salt, int iterationsCount, int keyLength) {
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").
                    generateSecret(new PBEKeySpec(password.toCharArray(), salt, iterationsCount, keyLength)).
                    getEncoded();
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static void main(String[] args) {
        try {
            new PasswordEncrypterMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
