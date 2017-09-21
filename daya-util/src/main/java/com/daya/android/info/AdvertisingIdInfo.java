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

public final class AdvertisingIdInfo {
    private AdvertisingIdInfo() {}

    @WorkerThread
    public static String getAdvertisingId(@NonNull Context context) {
        Validate.runningOnWorkerThread(
                AdvertisingIdInfo.class.getCanonicalName()
                        + "#getAdvertisingId() method should be called from the worker thread");

        String advertisingId = null;
        try {
            advertisingId = AdvertisingIdClient.getAdvertisingIdInfo(context).getId();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        return advertisingId;
    }
}
