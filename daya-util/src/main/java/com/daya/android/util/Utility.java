package com.daya.android.util;

import android.support.annotation.NonNull;

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

    /**
     * Returns whether the class with the given name exists.
     *
     * @param className The class name.
     * @return True if it exists, false otherwise.
     */
    public static boolean hasClass(@NonNull String className) {
        try {
            Class.forName(className);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
}
