/*
 * Copyright 2013-present NHN Entertainment Corp. All rights Reserved.
 * NHN Entertainment PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * @author shhong@nhnent.com
 */

package com.daya.android.info;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.daya.android.gms.AdvertisingIdClient;
import com.daya.android.util.Validate;

/**
 * Created by Shnoble on 2017. 9. 22..
 */

public final class AdvertisingInfo {
    private final String mAdvertisingId;
    private final boolean mIsLimitAdTrackingEnabled;

    private AdvertisingInfo(String advertisingId,
                            boolean isLimitAdTrackingEnabled) {
        this.mAdvertisingId = advertisingId;
        this.mIsLimitAdTrackingEnabled = isLimitAdTrackingEnabled;
    }

    public String getId() {
        return mAdvertisingId;
    }

    public boolean isLimitAdTrackingEnabled() {
        return mIsLimitAdTrackingEnabled;
    }

    @Override
    public String toString() {
        return "AdvertisingInfo: \n"
                + "- Advertising ID: " + mAdvertisingId
                + "- LimitAdTrackingEnabled: " + mIsLimitAdTrackingEnabled;
    }

    @WorkerThread
    public static AdvertisingInfo getAdvertisingInfo(@NonNull Context context) {
        Validate.runningOnWorkerThread(
                AdvertisingInfo.class.getCanonicalName()
                        + "#getAdvertisingInfo() method should be called from the worker thread");

        AdvertisingInfo advertisingInfo = null;
        try {
            AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(context);
            advertisingInfo = new AdvertisingInfo(info.getId(), info.isLimitAdTrackingEnabled());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return advertisingInfo;
    }
}
