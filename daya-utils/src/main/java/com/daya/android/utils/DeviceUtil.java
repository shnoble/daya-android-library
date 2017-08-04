package com.daya.android.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

/**
 * Created by shhong on 2017. 8. 4..
 */

public class DeviceUtil {

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getVersion() {
        return Build.VERSION.RELEASE;
    }

    public static DisplayMetrics getDisplayMetrics(final Context context) {
        return context.getResources().getDisplayMetrics();
    }
}
