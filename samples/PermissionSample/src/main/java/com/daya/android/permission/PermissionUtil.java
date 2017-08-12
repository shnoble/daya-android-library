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

    public static boolean hasPermission(@NonNull Context context,
                                        @NonNull String permission) {
        return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }

    public static boolean shouldShowRequestPermissionRationale(@NonNull Activity activity,
                                                               @NonNull String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    public static void requestPermissions(@NonNull Activity activity,
                                          @NonNull String[] permissions,
                                          int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
}
