package com.daya.activitylifecycle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by shhong on 2017. 12. 29..
 */

public class ChangedVolumeReceiver extends BroadcastReceiver {
    private static final String TAG = ChangedVolumeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG + "TaskTest", "onReceive");
    }
}
