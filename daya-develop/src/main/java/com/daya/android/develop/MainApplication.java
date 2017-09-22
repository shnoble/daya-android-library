package com.daya.android.develop;

import android.app.Application;
import android.util.Log;

/**
 * Created by shhong on 2017. 8. 10..
 */

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();

    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    public Thread.UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
        return mDefaultUncaughtExceptionHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Log.d("MainApplication", "" + mDefaultUncaughtExceptionHandler);

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    }

    private class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            Log.d(TAG, "Crash!!!");

            // Try everything to make sure this process goes away.
            // android.os.Process.killProcess(android.os.Process.myPid());
            // System.exit(10);
            mDefaultUncaughtExceptionHandler.uncaughtException(t, e);
        }
    }
}
