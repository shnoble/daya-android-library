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

import com.daya.android.util.Validate;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

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

    public boolean ismIsLimitAdTrackingEnabled() {
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
            if (info != null) {
                advertisingInfo = new AdvertisingInfo(info.getId(), info.isLimitAdTrackingEnabled());
            }

        } catch (IOException e) {
            // Unrecoverable error connecting to Google Play services (e.g.,
            // the old version of the service doesn't support getting AdvertisingId).
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // cannot be called on main thread
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Google Play services is not available entirely.
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            // Encountered a recoverable error connecting to Google Play services.
            e.printStackTrace();
        }

        return advertisingInfo;
    }
}
