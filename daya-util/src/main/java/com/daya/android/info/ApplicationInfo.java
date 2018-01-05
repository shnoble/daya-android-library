package com.daya.android.info;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

    /**
     * Return the label to use for this application.
     *
     * @param context The context.
     * @return The label of this application.
     */
    public static String getLabel(@NonNull Context context) {
        android.content.pm.ApplicationInfo applicationInfo = getApplicationInfo(context);
        if (applicationInfo == null) {
            return null;
        }
        return (String) context.getPackageManager().getApplicationLabel(applicationInfo);
    }

    /**
     * Return the version name to use for this application.
     *
     * @param context The context.
     * @return The version name of this application.
     */
    public static String getVersionName(@NonNull Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo == null) {
            return null;
        }
        return packageInfo.versionName;
    }

    /**
     * Returns the identifier of this process.
     */
    public static int getProcessId() {
        return android.os.Process.myPid();
    }

    private static android.content.pm.ApplicationInfo getApplicationInfo(@NonNull Context context) {
        String packageName = getPackageName(context);
        try {
            return context.getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static PackageInfo getPackageInfo(@NonNull Context context) {
        String packageName = getPackageName(context);
        try {
            return context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
