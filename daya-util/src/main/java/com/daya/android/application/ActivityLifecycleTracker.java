package com.daya.android.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.daya.android.util.DayaLog;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shhong on 2017. 12. 11..
 */

public class ActivityLifecycleTracker {
    private static final String TAG = ActivityLifecycleTracker.class.getSimpleName();

    private static AtomicInteger sActivityCount = new AtomicInteger(0);
    private static Set<LifecycleCallback> sLifecycleCallback = new CopyOnWriteArraySet<>();

    public static void startTracking(@NonNull Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (sActivityCount.getAndIncrement() == 0) {
                    onEnterForeground();
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (sActivityCount.decrementAndGet() <= 0) {
                    sActivityCount.set(0);

                    onEnterBackground();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private static void onEnterForeground() {
        DayaLog.d(TAG, "Enter foreground.");

        for (LifecycleCallback callback : sLifecycleCallback) {
            callback.onEnterForeground();
        }
    }

    private static void onEnterBackground() {
        DayaLog.d(TAG, "Enter background.");

        for (LifecycleCallback callback : sLifecycleCallback) {
            callback.onEnterBackground();
        }
    }

    public static boolean isBackground() {
        return sActivityCount.get() <= 0;
    }

    public static boolean isForeground() {
        return sActivityCount.get() > 0;
    }

    public static boolean registerCallback(@NonNull LifecycleCallback callback) {
        return sLifecycleCallback.add(callback);
    }

    public static boolean unregisterCallback(@NonNull LifecycleCallback callback) {
        return sLifecycleCallback.remove(callback);
    }

    public interface LifecycleCallback {
        void onEnterForeground();
        void onEnterBackground();
    }
}
