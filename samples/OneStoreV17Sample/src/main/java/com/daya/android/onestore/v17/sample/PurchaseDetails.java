package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

class PurchaseDetails {
    private final JSONObject mJsonObject;

    PurchaseDetails(@NonNull String jsonString) throws JSONException {
        mJsonObject = new JSONObject(jsonString);
    }

    public JSONObject getJSONObject() {
        return mJsonObject;
    }

    public String getPurchaseId() {
        return mJsonObject.optString("purchaseId");
    }

    @Override
    public String toString() {
        return mJsonObject.toString();
    }
}
