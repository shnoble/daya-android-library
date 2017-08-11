package com.daya.android.develop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.daya.android.analytics.DayaAnalytics;
import com.daya.android.analytics.DayaAnalyticsEvent;
import com.daya.android.utils.DeviceUtil;
import com.daya.android.utils.LocaleUtil;
import com.daya.android.utils.TelephonyUtil;
import com.daya.android.utils.TimeUtil;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

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
