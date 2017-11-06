/*
 * Copyright 2013-present NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author shhong@nhnent.com
 */

package com.daya.android.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by shhong on 2017. 6. 14..
 */

public abstract class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkReceiver.class.getSimpleName();
    private Integer mLastNetworkType;

    @Override
    public void onReceive(Context context, Intent intent) {
        debugIntent(intent);

        String action = intent.getAction();
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (!isTwiceCalled(context)) {
                onReceive(networkInfo);
            }
        }
    }

    private boolean isTwiceCalled(Context context) {
        int newType = NetworkManager.getType(context);
        if (mLastNetworkType != null) {
            // It is called twice on Android 4.x, so it will be ignored if it is the same type.
            if (mLastNetworkType == newType) {
                Log.d(TAG, "It is the same network type" +
                        "as the last received network type (" + mLastNetworkType + ").");
                return true;
            }
        }
        mLastNetworkType = newType;
        return false;
    }

    private void debugIntent(Intent intent) {
        StringBuilder builder = new StringBuilder()
                .append("=================== NetworkReceiver Intent Info ===================").append('\n')
                .append("action: ").append(intent.getAction()).append('\n')
                .append("component: ").append(intent.getComponent()).append('\n');

        Bundle extras = intent.getExtras();
        if (extras != null) {
            builder.append("extras: ").append('\n');
            for (String key : extras.keySet()) {
                builder.append("- ").append(key).append(": ").append(extras.get(key)).append('\n');
            }
        }
        builder.append("===================================================================");
        Log.v(TAG, builder.toString());
    }

    public abstract void onReceive(NetworkInfo networkInfo);
}

