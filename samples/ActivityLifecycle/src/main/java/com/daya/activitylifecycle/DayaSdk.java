package com.daya.activitylifecycle;

import android.app.Activity;
import android.app.Application;

import com.daya.android.application.ActivityLifecycleTracker;
import com.daya.android.util.DayaLog;

/**
 * Created by shhong on 2017. 12. 27..
 */

public class DayaSdk {
    private static final String TAG = DayaSdk.class.getSimpleName();
    private static boolean isSendSession = false;

    public static void initializeSdk(Application application) {
        ActivityLifecycleTracker.startTracking(application);
    }

    public static void initializeLogger() {
        DayaLog.d(TAG, "Initialize logger....");

        if (ActivityLifecycleTracker.getCreatedActivityCount() > 0) {
            sendSessionLog();
        } else {
            ActivityLifecycleTracker.registerActivityLifecycleCallbacks(new ActivityLifecycleTracker.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity) {
                    ActivityLifecycleTracker.unregisterActivityLifecycleCallbacks(this);
                    sendSessionLog();
                }

                @Override
                public void onActivityStarted(Activity activity) {}

                @Override
                public void onActivityResumed(Activity activity) {}

                @Override
                public void onActivityPaused(Activity activity) {}

                @Override
                public void onActivityStopped(Activity activity) {}

                @Override
                public void onActivityDestroyed(Activity activity) {}
            });
        }
    }

    private static void sendSessionLog() {
        if (!isSendSession) {
            DayaLog.d(TAG, "Send Session Log....");
            isSendSession = true;
        }
    }
}
