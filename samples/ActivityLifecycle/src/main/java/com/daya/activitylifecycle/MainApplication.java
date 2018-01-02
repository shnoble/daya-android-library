package com.daya.activitylifecycle;

import android.app.Application;

import com.daya.android.util.DayaLog;

/**
 * Created by shhong on 2017. 12. 11..
 */

public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        DayaLog.setLogLevel(DayaLog.DEBUG);
        DayaSdk.initializeSdk(this);

        TaskManager.printTasks(this);
    }
}
