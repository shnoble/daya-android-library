package com.daya.android.analytics;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by shhong on 2017. 8. 8..
 */

public class DayaAnalytics {

    private FirebaseAnalytics mFirebaseAnalytics;

    private DayaAnalytics(FirebaseAnalytics firebaseAnalytics) {
        this.mFirebaseAnalytics = firebaseAnalytics;
    }

    public static DayaAnalytics getInstance(final Context context) {
        return new DayaAnalytics(null);
    }
}
