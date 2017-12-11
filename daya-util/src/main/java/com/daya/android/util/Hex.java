package com.daya.android.util;

/**
 * Created by Shnoble on 2017. 12. 11..
 */

public class Hex {
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(Integer.toString((b & 0xf0) >> 4, 16));
            sb.append(Integer.toString((b & 0x0f), 16));
        }
        return sb.toString();
    }

    public static byte[] HexToBytes(String s) {
        int len = s.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) +
                    Character.digit(s.charAt(i + 1), 16));
        }
        return bytes;
    }
}
