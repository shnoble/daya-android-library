package com.daya.android.crypto;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Shnoble on 2017. 12. 10..
 */

public class AESCipher {
    private static final String ALGORITHM = "AES";
    private static final String ECB = "ECB";
    private static final String CBC = "CBC";

    public static byte[] encrypt(byte[] plainText, byte[] key) {
        String transformation = ALGORITHM + "/" + ECB + "/PKCS5Padding";
        return encrypt(transformation, plainText, key, null);
    }

    public static byte[] encrypt(byte[] plainText, byte[] key, byte[] iv) {
        String transformation = ALGORITHM + "/" + CBC + "/PKCS5Padding";
        return encrypt(transformation, plainText, key, iv);
    }

    private static byte[] encrypt(String transformation, byte[] plainText, byte[] key, byte[] iv) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = (iv != null) ? new IvParameterSpec(iv) : null;

            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(plainText);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] cipherText, byte[] key) {
        String transformation = ALGORITHM + "/" + ECB + "/PKCS5Padding";
        return decrypt(transformation, cipherText, key, null);
    }

    public static byte[] decrypt(byte[] cipherText, byte[] key, byte[] iv) {
        String transformation = ALGORITHM + "/" + CBC + "/PKCS5Padding";
        return decrypt(transformation, cipherText, key, iv);
    }

    private static byte[] decrypt(String transformation, byte[] cipherText, byte[] key, byte[] iv) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = (iv != null) ? new IvParameterSpec(iv) : null;

            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(cipherText);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] generateKey(int keySize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(keySize);
            return keyGenerator.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] generateIv() {
        byte[] iv = new byte[16];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        IvParameterSpec spec = new IvParameterSpec(iv);
        return spec.getIV();
    }
}
