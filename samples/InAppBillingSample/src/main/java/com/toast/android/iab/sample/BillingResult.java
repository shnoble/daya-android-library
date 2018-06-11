package com.toast.android.iab.sample;

import org.json.JSONException;
import org.json.JSONObject;

class BillingResult {
    private final int mCode;
    private final String mMessage;

    BillingResult(int code, String message) {
        mCode = code;
        mMessage = message;
    }

    boolean isSuccess() {
        return mCode == 0;
    }

    boolean isFailure() {
        return !isSuccess();
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
