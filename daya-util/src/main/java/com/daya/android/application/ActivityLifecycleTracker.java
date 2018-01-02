package com.daya.android.application;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.daya.android.util.DayaLog;

import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shhong on 2017. 12. 11..
 */

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class ActivityLifecycleTracker {
    private static final String TAG = ActivityLifecycleTracker.class.getSimpleName();

    private static AtomicInteger sCreatedActivityCount = new AtomicInteger(0);
    private static AtomicInteger sForegroundActivityCount = new AtomicInteger(0);
    private static AtomicBoolean sTracking = new AtomicBoolean(false);

    private static Set<ActivityLifecycleCallbacks> sActivityLifecycleCallbacks =
            new CopyOnWriteArraySet<>();
    private static Set<ApplicationLifecycleCallbacks> sApplicationLifecycleCallbacks =
            new CopyOnWriteArraySet<>();

    public static void startTracking(@NonNull Application application) {
        if (!sTracking.compareAndSet(false, true)) {
            return;
        }

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityLifecycleTracker.onActivityCreated(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                ActivityLifecycleTracker.onActivityStarted(activity);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                ActivityLifecycleTracker.onActivityResumed(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                ActivityLifecycleTracker.onActivityPaused(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
                ActivityLifecycleTracker.onActivityStopped(activity);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityLifecycleTracker.onActivityDestroyed(activity);
            }
        });
    }

    public static void onActivityCreated(Activity activity) {
        sCreatedActivityCount.incrementAndGet();

        debug(String.format(Locale.getDefault(), "Activity created(%d)", sCreatedActivityCount.get()));

        for (ActivityLifecycleCallbacks callback : sActivityLifecycleCallbacks) {
            callback.onActivityCreated(activity);
        }
    }

    public static void onActivityStarted(Activity activity) {
        if (sCreatedActivityCount.get() == 0) {
            onActivityCreated(activity);
        }

        debug(String.format(Locale.getDefault(), "Activity started(%d)", sCreatedActivityCount.get()));

        for (ActivityLifecycleCallbacks callback : sActivityLifecycleCallbacks) {
            callback.onActivityStarted(activity);
        }

        if (sForegroundActivityCount.getAndIncrement() == 0) {
            onEnterForeground();
        }
    }

    public static void onActivityResumed(Activity activity) {
        if (sCreatedActivityCount.get() == 0) {
            onActivityStarted(activity);
        }

        debug(String.format(Locale.getDefault(), "Activity resumed(%d)", sCreatedActivityCount.get()));

        for (ActivityLifecycleCallbacks callback : sActivityLifecycleCallbacks) {
            callback.onActivityResumed(activity);
        }
    }

    public static void onActivityPaused(Activity activity) {
        sCreatedActivityCount.compareAndSet(0, 1);

        debug(String.format(Locale.getDefault(), "Activity paused(%d)", sCreatedActivityCount.get()));

        for (ActivityLifecycleCallbacks callback : sActivityLifecycleCallbacks) {
            callback.onActivityPaused(activity);
        }
    }

    public static void onActivityStopped(Activity activity) {
        if (sCreatedActivityCount.get() == 0) {
            onActivityPaused(activity);
        }

        debug(String.format(Locale.getDefault(), "Activity stopped(%d)", sCreatedActivityCount.get()));

        for (ActivityLifecycleCallbacks callback : sActivityLifecycleCallbacks) {
            callback.onActivityStopped(activity);
        }

        if (sForegroundActivityCount.decrementAndGet() <= 0) {
            sForegroundActivityCount.set(0);

            onEnterBackground();
        }
    }

    public static void onActivityDestroyed(Activity activity) {
        if (sCreatedActivityCount.get() == 0) {
            onActivityStopped(activity);
        }

        if (sCreatedActivityCount.decrementAndGet() <= 0) {
            sCreatedActivityCount.set(0);
        }

        debug(String.format(Locale.getDefault(), "Activity destroyed(%d)", sCreatedActivityCount.get()));

        for (ActivityLifecycleCallbacks callback : sActivityLifecycleCallbacks) {
            callback.onActivityDestroyed(activity);
        }
    }

    private static void onEnterForeground() {
        debug("Enter foreground.");

        for (ApplicationLifecycleCallbacks callback : sApplicationLifecycleCallbacks) {
            callback.onEnterForeground();
        }
    }

    private static void onEnterBackground() {
        debug("Enter background.");

        for (ApplicationLifecycleCallbacks callback : sApplicationLifecycleCallbacks) {
            callback.onEnterBackground();
        }
    }

    public static int getCreatedActivityCount() {
        return sCreatedActivityCount.get();
    }

    public static boolean isBackground() {
        return sForegroundActivityCount.get() <= 0;
    }

    public static boolean isForeground() {
        return sForegroundActivityCount.get() > 0;
    }

    public static boolean registerActivityLifecycleCallbacks(
            @NonNull ActivityLifecycleCallbacks callback) {
        return sActivityLifecycleCallbacks.add(callback);
    }

    public static boolean unregisterActivityLifecycleCallbacks(
            @NonNull ActivityLifecycleCallbacks callback) {
        return sActivityLifecycleCallbacks.remove(callback);
    }

    public static boolean registerApplicationLifecycleCallbacks(
            @NonNull ApplicationLifecycleCallbacks callback) {
        return sApplicationLifecycleCallbacks.add(callback);
    }

    public static boolean unregisterApplicationLifecycleCallbacks(
            @NonNull ApplicationLifecycleCallbacks callback) {
        return sApplicationLifecycleCallbacks.remove(callback);
    }

    private static void debug(String s) {
        DayaLog.d(TAG, s);
    }

    public interface ActivityLifecycleCallbacks {
        void onActivityCreated(Activity activity);
        void onActivityStarted(Activity activity);
        void onActivityResumed(Activity activity);
        void onActivityPaused(Activity activity);
        void onActivityStopped(Activity activity);
        void onActivityDestroyed(Activity activity);
    }

    public interface ApplicationLifecycleCallbacks {
        void onEnterForeground();
        void onEnterBackground();
    }
}
