package com.daya.android.info;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

/**
 * Created by shhong on 2017. 8. 3..
 */

public final class TelephonyInfo {
    private TelephonyInfo() {}

    private static TelephonyManager getTelephonyManager(@NonNull Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * Returns the unique device ID, for example, the IMEI for GSM and the MEID
     * or ESN for CDMA phones. Return null if device ID is not available.
     *
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     *
     * @param context The context.
     * @return The device ID.
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceId(@NonNull Context context) {
        TelephonyManager telephonyManager = getTelephonyManager(context);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getDeviceId();
    }

    /**
     * Returns the numeric name (MCC+MNC) of current registered operator.
     * <p>
     * Availability: Only when user is registered to a network. Result may be
     * unreliable on CDMA networks (use {@link TelephonyManager#getPhoneType()} to determine if
     * on a CDMA network).
     *
     * @param context The context.
     * @return The numeric name (MCC+MNC) of current registered operator.
     */
    public static String getNetworkOperator(@NonNull Context context) {
        TelephonyManager telephonyManager = getTelephonyManager(context);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getNetworkOperator();
    }

    /**
     * Returns the alphabetic name of current registered operator.
     * <p>
     * Availability: Only when user is registered to a network. Result may be
     * unreliable on CDMA networks (use {@link TelephonyManager#getPhoneType()} to determine if
     * on a CDMA network).
     *
     * @param context The context.
     * @return The alphabetic name of current registered operator.
     */
    public static String getNetworkOperatorName(@NonNull Context context) {
        TelephonyManager telephonyManager = getTelephonyManager(context);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getNetworkOperatorName();
    }

    /**
     * Returns the ISO country code equivalent of the current registered
     * operator's MCC (Mobile Country Code).
     * <p>
     * Availability: Only when user is registered to a network. Result may be
     * unreliable on CDMA networks (use {@link TelephonyManager#getPhoneType()} to determine if
     * on a CDMA network).
     *
     * @param context The context.
     * @return The ISO country code.
     */
    public static String getNetworkCountryIso(@NonNull Context context) {
        TelephonyManager telephonyManager = getTelephonyManager(context);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getNetworkCountryIso();
    }

    /**
     * Returns the MCC+MNC (mobile country code + mobile network code) of the
     * provider of the SIM. 5 or 6 decimal digits.
     *
     * @param context The context.
     * @return The MCC+MNC (mobile country code + mobile network code).
     */
    public static String getSimOperator(@NonNull Context context) {
        TelephonyManager telephonyManager = getTelephonyManager(context);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getSimOperator();
    }

    /**
     * Returns the Service Provider Name (SPN).
     *
     * @param context The context.
     * @return The service provider name.
     */
    public static String getSimOperatorName(@NonNull Context context) {
        TelephonyManager telephonyManager = getTelephonyManager(context);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getSimOperatorName();
    }

    /**
     * Returns the ISO country code equivalent for the SIM provider's country code.
     *
     * @param context The context.
     * @return The ISO country code.
     */
    public static String getSimCountryIso(@NonNull Context context) {
        TelephonyManager telephonyManager = getTelephonyManager(context);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getSimCountryIso();
    }

    /**
     * Returns the serial number of the SIM, if applicable. Return null if it is
     * unavailable.
     * <p>
     * Requires Permission:
     *   {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     *
     * @param context The context.
     * @return The serial number of the SIM.
     */
    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getSimSerialNumber(@NonNull Context context) {
        TelephonyManager telephonyManager = getTelephonyManager(context);
        if (telephonyManager == null) {
            return null;
        }

        return telephonyManager.getSimSerialNumber();
    }
}
