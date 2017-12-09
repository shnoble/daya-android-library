package com.daya.android.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Shnoble on 2017. 12. 10..
 */

public class RSAKeyGenerator {

    private static final String ALGORITHM = "RSA";
    private static final int DEFAULT_KEY_SIZE = 1024;

    public static KeyPair generateKeyPair() {
        return generateKeyPair(DEFAULT_KEY_SIZE);
    }

    public static KeyPair generateKeyPair(int keySize) {
        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance(ALGORITHM);
            keyGenerator.initialize(keySize, new SecureRandom());
            return keyGenerator.generateKeyPair();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
