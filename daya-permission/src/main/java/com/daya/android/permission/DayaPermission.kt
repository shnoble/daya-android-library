package com.daya.android.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class DayaPermission {
    companion object {
        /**
         * Determine whether you have been granted a particular permission.
         *
         * @param context The context.
         * @param permission The name of the permission being checked.
         * @return true if you have the permission, or false if not.
         */
        fun hasPermission(
            context: Context,
            permission: String
        ): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            } else {
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }
        }

        /**
         * Gets whether you should show UI with rationale for requesting a permission.
         *
         * @param activity The target activity.
         * @param permission A permission your app wants to request.
         * @return Whether you can show permission rationale UI.
         */
        fun shouldShowRequestPermissionRationale(
            activity: Activity,
            permission: String
        ): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.shouldShowRequestPermissionRationale(permission)
            } else {
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
            }
        }

        /**
         * Requests permissions to be granted to this application.
         *
         * @param activity The target activity.
         * @param permissions The requested permissions. Must me non-null and not empty.
         * @param requestCode Application specific request code to match with a result
         * reported to [ActivityCompat.OnRequestPermissionsResultCallback.onRequestPermissionsResult]
         * Should be >= 0.
         */
        fun requestPermissions(
            activity: Activity,
            permissions: Array<String?>,
            requestCode: Int
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(permissions, requestCode)
            } else {
                ActivityCompat.requestPermissions(activity, permissions, requestCode)
            }
        }
    }
}