package com.daya.android.util;

/**
 * Created by shhong on 2017. 9. 21..
 */

public class UUID {

    /**
     * Returns a type 4 (pseudo randomly generated) UUID.
     *
     * @return  A randomly generated UUID
     */
    public static String randomUUID() {
        return java.util.UUID.randomUUID().toString();
    }
}
