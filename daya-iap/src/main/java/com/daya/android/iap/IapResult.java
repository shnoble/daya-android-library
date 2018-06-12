package com.daya.android.iap;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class IapResult {
    public static final int RESULT_FEATURE_NOT_SUPPORTED = -2;
    public static final int RESULT_SERVICE_DISCONNECTED = -1;
    public static final int RESULT_OK = 0;
    public static final int RESULT_USER_CANCELED = 1;
    public static final int RESULT_SERVICE_UNAVAILABLE = 2;
    public static final int RESULT_BILLING_UNAVAILABLE = 3;
    public static final int RESULT_ITEM_UNAVAILABLE = 4;
    public static final int RESULT_DEVELOPER_ERROR = 5;
    public static final int RESULT_ERROR = 6;
    public static final int RESULT_ITEM_ALREADY_OWNED = 7;
    public static final int RESULT_ITEM_NOT_OWNED = 8;
    public static final int RESULT_NEED_LOGIN = 9;
    public static final int RESULT_NEED_UPDATE = 10;
    public static final int RESULT_SECURITY_ERROR = 11;

    public static final int IAP_REMOTE_EXCEPTION = -1001;
    public static final int IAP_BAD_RESPONSE = -1002;
    public static final int IAP_VERIFICATION_FAILED = -1003;
    public static final int IAP_SEND_INTENT_FAILED = -1004;
    public static final int IAP_UNKNOWN_PURCHASE_RESPONSE = -1006;
    public static final int IAP_MISSING_TOKEN = -1007;
    public static final int IAP_UNKNOWN_ERROR = -1008;
    public static final int IAP_SUBSCRIPTIONS_NOT_AVAILABLE = -1009;
    public static final int IAP_INVALID_CONSUMPTION = -1010;
    public static final int IAP_SUBSCRIPTION_UPDATE_NOT_AVAILABLE = -1011;

    private final int mCode;
    private final String mMessage;

    IapResult(int code, @NonNull String message) {
        mCode = code;
        mMessage = message;
    }

    int getCode() {
        return mCode;
    }

    String getMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        try {
            return new JSONObject()
                    .putOpt("code", mCode)
                    .putOpt("message", mMessage)
                    .toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
}
