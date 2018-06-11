package com.daya.android.iap.onestore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetails {
    private final String mPrice;
    private final String mProductId;
    private final String mType;
    private final String mTitle;

    ProductDetails(@NonNull String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        mPrice = jsonObject.optString("price");
        mProductId = jsonObject.optString("productId");
        mType = jsonObject.optString("type");
        mTitle = jsonObject.optString("title");
    }

    @Nullable
    public String getPrice() {
        return mPrice;
    }

    @Nullable
    public String getProductId() {
        return mProductId;
    }

    @Nullable
    public String getType() {
        return mType;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }
}
