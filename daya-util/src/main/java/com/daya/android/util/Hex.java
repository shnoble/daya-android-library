package com.daya.android.util;

import java.math.BigInteger;

/**
 * Created by Shnoble on 2017. 12. 11..
 */

public class Hex {
    public static String bytesToHex(byte[] bytes) {
        return new BigInteger(1, bytes).toString(16);
    }

    public static byte[] HexToBytes(String hex) {
        return new BigInteger(hex, 16).toByteArray();
    }
}
