package com.daya.android.info;

import android.os.Build;

/**
 * Created by shhong on 2017. 9. 21..
 */

public class OperatingSystemInfo {

    private static final String ANDROID_OPERATING_SYSTEM_NAME = "Android";

    /**
     * Returns the OS name.
     *
     * @return The OS name.
     */
    public static String getName() {
        return ANDROID_OPERATING_SYSTEM_NAME;
    }

    /**
     * Returns the OS version.
     *
     * @return The OS version.
     */
    public static String getVersion() {
        return Build.VERSION.RELEASE;
    }
}
