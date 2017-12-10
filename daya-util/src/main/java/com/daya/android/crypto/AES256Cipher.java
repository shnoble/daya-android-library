package com.daya.android.crypto;

/**
 * Created by Shnoble on 2017. 12. 11..
 */

public class AES256Cipher extends AESCipher {
    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 16;

    public static byte[] generateKey() {
        return generateKey(KEY_SIZE);
    }

    public static byte[] generateIv() {
        return generateIv(IV_SIZE);
    }
}
