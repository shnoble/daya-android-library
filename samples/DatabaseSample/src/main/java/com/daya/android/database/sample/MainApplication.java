package com.daya.android.database.sample;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Shnoble on 2018. 2. 25..
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
