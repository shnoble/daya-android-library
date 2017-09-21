package com.daya.android.info;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * Created by shhong on 2017. 8. 4..
 */

public class DeviceInfo {

    /**
     * Returns the manufacturer of the product/hardware.
     *
     * @return The manufacturer.
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * Returns the device model.
     *
     * @return The device model.
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }


    /**
     * Return the current display metrics that are in effect for this resource
     * object.  The returned object should be treated as read-only.
     *
     * @param context The context.
     * @return The resource's current display metrics.
     */
    public static DisplayMetrics getDisplayMetrics(@NonNull Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * Return the ANDROID_ID.
     *
     * @param context The context.
     * @return ANDROID_ID.
     */
    public static String getAndroidId(@NonNull Context context) {
        // Using device identifiers is not recommended other than for high value fraud prevention and advanced telephony use-cases.
        // For advertising use-cases, use AdvertisingIdClient$Info#getId and for analytics, use InstanceId#getId.
        String androidId =
                Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO &&
                androidId != null &&
                !"0000000000000000".equals(androidId) &&
                !"9774d56d682e549c".equals(androidId)) {
            return androidId;
        }
        return null;
    }
}
