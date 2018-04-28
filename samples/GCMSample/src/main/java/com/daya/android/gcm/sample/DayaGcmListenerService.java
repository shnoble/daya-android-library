package com.daya.android.gcm.sample;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Shnoble on 2018. 4. 29..
 */

public class DayaGcmListenerService extends GcmListenerService {
    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        super.onMessageReceived(s, bundle);
    }
}
