package com.daya.android.util;

import java.util.UUID;

/**
 * Created by shhong on 2017. 9. 21..
 */

public final class Utility {
    private Utility() {}

    /**
     * Returns a type 4 (pseudo randomly generated) UUID.
     *
     * @return  A randomly generated UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }
}
