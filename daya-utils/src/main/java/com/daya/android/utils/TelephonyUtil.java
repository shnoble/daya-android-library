package com.daya.android.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by shhong on 2017. 8. 3..
 */

public class TelephonyUtil {

    public static String getDeviceId(final Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getDeviceId();
    }

    public static String getNetworkOperatorName(final Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getNetworkOperatorName();
    }

    public static String getNetworkCountryIso(final Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getNetworkCountryIso();
    }
}
