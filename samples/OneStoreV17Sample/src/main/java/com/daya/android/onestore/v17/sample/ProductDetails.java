package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

class ProductDetails {
    private final JSONObject mJsonObject;

    ProductDetails(@NonNull String jsonString) throws JSONException {
        mJsonObject = new JSONObject(jsonString);
    }

    @Override
    public String toString() {
        return mJsonObject.toString();
    }
}
