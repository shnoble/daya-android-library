package com.daya.android.iap.google.billing;

import android.support.annotation.NonNull;
import android.util.Log;

class BillingLog {
    static void d(@NonNull String tag, @NonNull String msg) {
        Log.d(tag, msg);
    }
}
