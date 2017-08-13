package com.daya.android.permission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Shnoble on 2017. 8. 13..
 */

public class PermissionUtil {

    /**
     * Determine whether you have been granted a particular permission.
     *
     * @param permission The name of the permission being checked.
     * @return true if you have the permission, or false if not.
     */
    public static boolean hasPermission(@NonNull Context context,
                                        @NonNull String permission) {
        return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Gets whether you should show UI with rationale for requesting a permission.
     *
     * @param activity The target activity.
     * @param permission A permission your app wants to request.
     * @return Whether you can show permission rationale UI.
     */
    public static boolean shouldShowRequestPermissionRationale(@NonNull Activity activity,
                                                               @NonNull String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     * Requests permissions to be granted to this application.
     *
     * @param activity The target activity.
     * @param permissions The requested permissions. Must me non-null and not empty.
     * @param requestCode Application specific request code to match with a result
     *    reported to {@link ActivityCompat.OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])}
     *    Should be >= 0.
     */
    public static void requestPermissions(@NonNull Activity activity,
                                          @NonNull String[] permissions,
                                          int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
}
