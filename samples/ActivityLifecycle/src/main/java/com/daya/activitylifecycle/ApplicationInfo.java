package com.daya.activitylifecycle;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by shhong on 2017. 12. 11..
 */

public class ApplicationInfo {
    private static Context sApplicationContext;

    private ApplicationInfo() {}

    public static Context getApplicationContext() {
        return sApplicationContext;
    }

    public static void setApplicationContext(@NonNull Context applicationContext) {
        sApplicationContext = applicationContext.getApplicationContext();
    }
}
