package com.daya.android.permission.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.daya.android.permission.PermissionUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String permission = Manifest.permission.WRITE_CALENDAR;
        if (PermissionUtil.hasPermission(this, Manifest.permission.WRITE_CALENDAR)) {
            Log.d(TAG, permission + " permission granted.");
        } else {
            Log.w(TAG, permission + " permission denied.");

            if (PermissionUtil.shouldShowRequestPermissionRationale(this, permission)) {
                Log.d(TAG, "shouldShowRequestPermissionRationale: true");
                PermissionUtil.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
            } else {
                Log.d(TAG, "shouldShowRequestPermissionRationale: false");
                PermissionUtil.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                Log.d(TAG, "Request Permissions Result:");
                for (int i = 0; i < permissions.length; i++) {
                    String result = (grantResults[i] == PackageManager.PERMISSION_GRANTED) ? "PERMISSION_GRANTED" : "PERMISSION_DENIED";
                    Log.d(TAG, "- " + permissions[i] + ": " + result);
                }
                break;
        }
    }
}
