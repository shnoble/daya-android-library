package com.daya.android.analytics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by shhong on 2017. 8. 8..
 */

public class DayaAnalytics {

    private static final Object mLock = new Object();
    private static DayaAnalytics mInstance;

    private final FirebaseAnalytics mFirebaseAnalytics;

    private DayaAnalytics(@NonNull Context applicationContext) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(applicationContext);
    }

    @RequiresPermission(
            allOf = {
                    "android.permission.INTERNET",
                    "android.permission.ACCESS_NETWORK_STATE",
                    "android.permission.WAKE_LOCK"
            }
    )
    @Keep
    public static DayaAnalytics getInstance(@NonNull final Context context) {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new DayaAnalytics(context.getApplicationContext());
            }
            return mInstance;
        }
    }


    public void logEvent(@NonNull DayaAnalyticsEvent event) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, event.getItemId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event.getItemName());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, event.getContentType());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
