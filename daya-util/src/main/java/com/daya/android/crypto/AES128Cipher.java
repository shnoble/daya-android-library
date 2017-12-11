package com.daya.android.crypto;

/**
 * Created by shhong on 2017. 12. 11..
 */

public class AES128Cipher extends AESCipher {
    private static final int KEY_SIZE = 128;

    public static byte[] generateKey() {
        return generateKey(KEY_SIZE);
    }
}
