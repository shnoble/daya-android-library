package com.daya.android.crypto;

/**
 * Created by Shnoble on 2017. 12. 11..
 */

public class AES256Cipher extends AESCipher {
    private static final int KEY_SIZE = 256;

    public static byte[] generateKey() {
        return generateKey(KEY_SIZE);
    }
}
