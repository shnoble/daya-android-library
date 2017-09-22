package com.daya.android.util;

import android.os.Looper;

/**
 * Created by shhong on 2017. 9. 15..
 */

public final class Validate {
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

    /**
     * Ensures that the method works in the UI thread.
     *
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *        string using {@link String#valueOf(Object)}
     */
    public static void runningOnUiThread(Object errorMessage) {
        if (!Looper.getMainLooper().equals(Looper.myLooper())) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    /**
     * Ensures that the method works in the worker thread.
     *
     * @param errorMessage the exception message to use if the check fails; will be converted to a
     *        string using {@link String#valueOf(Object)}
     */
    public static void runningOnWorkerThread(Object errorMessage) {
        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }
}
