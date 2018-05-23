package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;
import android.util.Log;

class OneStoreLog {
    static void d(@NonNull String tag, @NonNull String msg) {
        Log.d(tag, msg);
    }

    static void e(@NonNull String tag, @NonNull String msg) {
        Log.e(tag, msg);
    }
}
