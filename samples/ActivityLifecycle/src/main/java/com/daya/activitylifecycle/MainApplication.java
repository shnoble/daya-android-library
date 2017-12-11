package com.daya.activitylifecycle;

import android.app.Application;

/**
 * Created by shhong on 2017. 12. 11..
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationInfo.setApplicationContext(getApplicationContext());
    }
}
