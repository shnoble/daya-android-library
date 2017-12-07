package com.daya.android.util;

import android.util.Log;

/**
 * Created by shhong on 2017. 12. 7..
 */

public class DayaLog {
    // Grabbing the native values from Android's native logging facilities,
    // to make for easy migration and interop.
    public static final int VERBOSE = android.util.Log.VERBOSE;
    public static final int DEBUG = android.util.Log.DEBUG;
    public static final int INFO = android.util.Log.INFO;
    public static final int WARN = android.util.Log.WARN;
    public static final int ERROR = android.util.Log.ERROR;
    public static final int ASSERT = android.util.Log.ASSERT;

    private static int sLevel;

    static {
        sLevel = WARN;
    }

    public static int getLogLevel() {
        return sLevel;
    }

    public static void setLogLevel(int level) {
        sLevel = level;
    }

    static boolean isLoggable(int priority) {
        return priority >= sLevel;
    }

    /**
     * Instructs the LogNode to print the log data provided. Other LogNodes can
     * be chained to the end of the LogNode as desired.
     *
     * @param priority Logger level of the data being logged. Verbose, Error, etc.
     * @param tag      Tag for for the log data. Can be used to organize log statements.
     * @param msg      The actual message to be logged.
     * @param tr       If an exception was thrown, this can be sent along for the logging facilities
     *                 to extract and print useful information.
     */
    public static void println(int priority, String tag, String msg, Throwable tr) {
        if (!isLoggable(priority)) {
            return;
        }

        String useMsg = "[TOAST] ";
        if (msg != null) {
            useMsg += msg;
        }

        if (tr != null) {
            useMsg += "\n" + Log.getStackTraceString(tr);
        }

        android.util.Log.println(priority, tag, useMsg);
    }

    /**
     * Instructs the LogNode to print the log data provided. Other LogNodes can
     * be chained to the end of the LogNode as desired.
     *
     * @param priority Logger level of the data being logged. Verbose, Error, etc.
     * @param tag      Tag for for the log data. Can be used to organize log statements.
     * @param msg      The actual message to be logged. The actual message to be logged.
     */
    public static void println(int priority, String tag, String msg) {
        println(priority, tag, msg, null);
    }

    /**
     * Prints a message at VERBOSE priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr  If an exception was thrown, this can be sent along for the logging facilities
     *            to extract and print useful information.
     */
    public static void v(String tag, String msg, Throwable tr) {
        println(VERBOSE, tag, msg, tr);
    }

    /**
     * Prints a message at VERBOSE priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void v(String tag, String msg) {
        v(tag, msg, null);
    }


    /**
     * Prints a message at DEBUG priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr  If an exception was thrown, this can be sent along for the logging facilities
     *            to extract and print useful information.
     */
    public static void d(String tag, String msg, Throwable tr) {
        println(DEBUG, tag, msg, tr);
    }

    /**
     * Prints a message at DEBUG priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void d(String tag, String msg) {
        d(tag, msg, null);
    }

    /**
     * Prints a message at INFO priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr  If an exception was thrown, this can be sent along for the logging facilities
     *            to extract and print useful information.
     */
    public static void i(String tag, String msg, Throwable tr) {
        println(INFO, tag, msg, tr);
    }

    /**
     * Prints a message at INFO priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void i(String tag, String msg) {
        i(tag, msg, null);
    }

    /**
     * Prints a message at WARN priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr  If an exception was thrown, this can be sent along for the logging facilities
     *            to extract and print useful information.
     */
    public static void w(String tag, String msg, Throwable tr) {
        println(WARN, tag, msg, tr);
    }

    /**
     * Prints a message at WARN priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void w(String tag, String msg) {
        w(tag, msg, null);
    }

    /**
     * Prints a message at WARN priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param tr  If an exception was thrown, this can be sent along for the logging facilities
     *            to extract and print useful information.
     */
    public static void w(String tag, Throwable tr) {
        w(tag, null, tr);
    }

    /**
     * Prints a message at ERROR priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr  If an exception was thrown, this can be sent along for the logging facilities
     *            to extract and print useful information.
     */
    public static void e(String tag, String msg, Throwable tr) {
        println(ERROR, tag, msg, tr);
    }

    /**
     * Prints a message at ERROR priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void e(String tag, String msg) {
        e(tag, msg, null);
    }

    /**
     * Prints a message at ASSERT priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     * @param tr  If an exception was thrown, this can be sent along for the logging facilities
     *            to extract and print useful information.
     */
    public static void wtf(String tag, String msg, Throwable tr) {
        println(ASSERT, tag, msg, tr);
    }

    /**
     * Prints a message at ASSERT priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param msg The actual message to be logged.
     */
    public static void wtf(String tag, String msg) {
        wtf(tag, msg, null);
    }

    /**
     * Prints a message at ASSERT priority.
     *
     * @param tag Tag for for the log data. Can be used to organize log statements.
     * @param tr  If an exception was thrown, this can be sent along for the logging facilities
     *            to extract and print useful information.
     */
    public static void wtf(String tag, Throwable tr) {
        wtf(tag, null, tr);
    }
}
