package com.toast.android.develop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.daya.android.utils.DeviceUtil;
import com.daya.android.utils.LocaleUtil;
import com.daya.android.utils.TelephonyUtil;
import com.daya.android.utils.TimeUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "<<< Device Info >>>");
        Log.d(TAG, "Device Manufacturer: " + DeviceUtil.getManufacturer());
        Log.d(TAG, "Device Model: " + DeviceUtil.getModel());
        Log.d(TAG, "Device Version: " + DeviceUtil.getVersion());
        Log.d(TAG, "Display Metrics: " + DeviceUtil.getDisplayMetrics(getApplicationContext()));

        Log.d(TAG, "<<< Locale Info >>>");
        Log.d(TAG, "Locale Language: " + LocaleUtil.getLanguage());
        Log.d(TAG, "Locale Country: " + LocaleUtil.getCountry());

        Log.d(TAG, "<<< Time Info >>>");
        Log.d(TAG, "Time Zone ID: " + TimeUtil.getTimeZoneId());

        Log.d(TAG, "<<< Telephony Info >>>");
        Log.d(TAG, "Telephony Network Operation Name: " + TelephonyUtil.getNetworkOperatorName(getApplicationContext()));
        Log.d(TAG, "Telephony Network Country ISO: " + TelephonyUtil.getNetworkCountryIso(getApplicationContext()));

        /*String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                int result = PermissionChecker.checkSelfPermission(this, permission);

                if (result == PermissionChecker.PERMISSION_GRANTED) {

                } else {
                    doRequestPermission();
                }
            }
        }

        String deviceId = TelephonyUtil.getDeviceId(getApplicationContext());
        Log.d(TAG, "Device ID: " + deviceId);*/
    }

    private void doRequestPermission() {
        /*String[] permissions = new String[]{Manifest.permission.READ_PHONE_STATE};

        ArrayList<String> notGrantedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (!PermissionUtils.hasPermissions(this, permission)) {
                notGrantedPermissions.add(permission);
            }
        }

        ActivityCompat.requestPermissions(this, notGrantedPermissions.toArray(new String[]{}), PermissionUtils.MUST_HAVE_REQUEST_CODE);*/
    }
}
