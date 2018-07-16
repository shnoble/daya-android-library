package com.daya.android.iap.onestore.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetails {
    private JSONObject mOriginJson;
    private final String mPrice;
    private final String mProductId;
    private final String mType;
    private final String mTitle;

    ProductDetails(@NonNull String jsonString) throws JSONException {
        mOriginJson = new JSONObject(jsonString);
        mPrice = mOriginJson.getString("price");
        mProductId = mOriginJson.getString("productId");
        mType = mOriginJson.getString("type");
        mTitle = mOriginJson.optString("title");
    }

    @NonNull
    public String getPrice() {
        return mPrice;
    }

    @NonNull
    public String getProductId() {
        return mProductId;
    }

    @NonNull
    public String getType() {
        return mType;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String toString() {
        try {
            return mOriginJson.toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
}
