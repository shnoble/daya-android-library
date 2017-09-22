package com.daya.android.info;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by shhong on 2017. 9. 21..
 */

public final class ApplicationInfo {
    private ApplicationInfo() {}

    /**
     * Return the name of this application's package.
     *
     * @param context The context.
     * @return The name of package name.
     */
    public static String getPackageName(@NonNull Context context) {
        return context.getPackageName();
    }
}
