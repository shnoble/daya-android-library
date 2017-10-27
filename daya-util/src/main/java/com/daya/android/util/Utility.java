package com.daya.android.util;

import android.os.Handler;
import android.os.Looper;
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

    /**
     * Returns whether the given string object is null or empty.
     *
     * @param s The string.
     * @return True if it is null or empty, false otherwise.
     */
    public static boolean isNullOrEmpty(String s) {
        return (s == null) || s.isEmpty();
    }

    /**
     * Runs the specified action on the UI thread. If the current thread is the UI
     * thread, then the action is executed immediately. If the current thread is
     * not the UI thread, the action is posted to the event queue of the UI thread.
     *
     * @param action the action to run on the UI thread
     */
    public static void runOnUiThread(@NonNull Runnable action) {
        if (!Looper.getMainLooper().equals(Looper.myLooper())) {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(action);
        } else {
            action.run();
        }
    }
}
