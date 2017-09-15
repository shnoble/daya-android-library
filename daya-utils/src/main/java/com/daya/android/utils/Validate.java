package com.daya.android.utils;

/**
 * Created by shhong on 2017. 9. 15..
 */

public class Validate {
    private Validate() {}

    /**
     * Ensures that an object reference passed as a parameter to the calling method is not null.
     *
     * @param reference an object reference
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *        string using {@link String#valueOf(Object)}
     */
    public static void notNull(Object reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
    }
}
