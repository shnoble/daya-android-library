package com.daya.android.develop;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.daya.android.analytics.DayaAnalytics;
import com.daya.android.analytics.DayaAnalyticsEvent;
import com.daya.android.info.ApplicationInfo;
import com.daya.android.info.DeviceInfo;
import com.daya.android.info.LocaleInfo;
import com.daya.android.info.OperatingSystemInfo;
import com.daya.android.info.TelephonyInfo;
import com.daya.android.info.TimeZoneInfo;
import com.daya.android.network.NetworkManager;
import com.daya.android.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context context = getApplicationContext();

        Log.d(TAG, "<<< Application Info >>>");
        Log.d(TAG, "Package Name: " + ApplicationInfo.getPackageName(context));

        Log.d(TAG, "<<< OS Info >>>");
        Log.d(TAG, "OS Name: " + OperatingSystemInfo.getName());
        Log.d(TAG, "OS Version: " + OperatingSystemInfo.getVersion());

        Log.d(TAG, "<<< Device Info >>>");
        Log.d(TAG, "Device Manufacturer: " + DeviceInfo.getManufacturer());
        Log.d(TAG, "Device Model: " + DeviceInfo.getDeviceModel());
        Log.d(TAG, "Display Metrics: " + DeviceInfo.getDisplayMetrics(context));
        Log.d(TAG, "Android ID (ANDROID_ID): " + DeviceInfo.getAndroidId(context));

        Log.d(TAG, "<<< Locale Info >>>");
        Log.d(TAG, "Locale Language: " + LocaleInfo.getLanguage());
        Log.d(TAG, "Locale Country: " + LocaleInfo.getCountry());

        Log.d(TAG, "<<< Time Info >>>");
        Log.d(TAG, "Time Zone ID: " + TimeZoneInfo.getId());

        Log.d(TAG, "<<< Telephony Info >>>");
        Log.d(TAG, "Telephony Network Operator: " + TelephonyInfo.getNetworkOperator(context));
        Log.d(TAG, "Telephony Network Operator Name: " + TelephonyInfo.getNetworkOperatorName(context));
        Log.d(TAG, "Telephony Network Country ISO: " + TelephonyInfo.getNetworkCountryIso(context));
        Log.d(TAG, "Telephony SIM Operator: " + TelephonyInfo.getSimOperator(context));
        Log.d(TAG, "Telephony SIM Operator Name: " + TelephonyInfo.getSimOperatorName(context));
        Log.d(TAG, "Telephony SIM Country ISO: " + TelephonyInfo.getSimCountryIso(context));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Telephony SIM Serial Number: Permission denied");
            Log.d(TAG, "Telephony Device ID: Permission denied");
        } else {
            Log.d(TAG, "Telephony SIM Serial Number: " + TelephonyInfo.getSimSerialNumber(context));
            Log.d(TAG, "Telephony Device ID: " + TelephonyInfo.getDeviceId(context));
        }

        Log.d(TAG, "<<< Network Info >>>");
        Log.d(TAG, "Network is Available: " + NetworkManager.isAvailable(context));
        Log.d(TAG, "Network is Connected: " + NetworkManager.isConnected(context));
        Log.d(TAG, "Network is Connected or Connecting: " + NetworkManager.isConnectedOrConnecting(context));
        Log.d(TAG, "Network Type: " + NetworkManager.getType(context));
        Log.d(TAG, "Network Type Name: " + NetworkManager.getTypeName(context));

        Log.d(TAG, "<<< Util >>>");
        Log.d(TAG, "Random UUID: " + UUID.randomUUID());
    }

    public void onSendEvent(View view) {
        DayaAnalyticsEvent event = new DayaAnalyticsEvent();
        event.setItemId("itemId");
        event.setItemName("itemName");
        event.setContentType("contentType");

        DayaAnalytics analytics = DayaAnalytics.getInstance(this);
        analytics.logEvent(event);
    }

    public void onCrashMain(View view) {
        // Make a Arithmetic Exception!!
        // Divide by zero
        @SuppressWarnings("unused")
        int a = 10 / 0;
    }

    public void onCrashThread(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Make a Arithmetic Exception!!
                // Divide by zero
                @SuppressWarnings("unused")
                int a = 10 / 0;
            }
        }).start();
    }
}
