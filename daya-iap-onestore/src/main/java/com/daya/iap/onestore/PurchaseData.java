package com.daya.iap.onestore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseData {
    private final String mOrderId;
    private final String mPackageName;
    private final String mProductId;
    private final long mPurchaseTime;
    private final String mPurchaseId;
    private final int mPurchaseState;
    private final int mRecurringState;
    private final String mDeveloperPayload;
    private final String mPurchaseDetails;
    private final String mPurchaseSignature;

    PurchaseData(@NonNull String purchaseDetails,
                 @NonNull String purchaseSignature) throws JSONException {
        JSONObject jsonObject = new JSONObject(purchaseDetails);
        mOrderId = jsonObject.optString("orderId");
        mPackageName = jsonObject.optString("packageName");
        mProductId = jsonObject.optString("productId");
        mPurchaseTime = jsonObject.optLong("purchaseTime");
        mPurchaseId = jsonObject.optString("purchaseId");
        mPurchaseState = jsonObject.optInt("purchaseState");
        mRecurringState = jsonObject.optInt("recurringState");
        mDeveloperPayload = jsonObject.optString("developerPayload");
        mPurchaseDetails = purchaseDetails;
        mPurchaseSignature = purchaseSignature;
    }

    @Nullable
    public String getOrderId() {
        return mOrderId;
    }

    @Nullable
    public String getPackageName() {
        return mPackageName;
    }

    @Nullable
    public String getProductId() {
        return mProductId;
    }

    public long getPurchaseTime() {
        return mPurchaseTime;
    }

    @Nullable
    public String getPurchaseId() {
        return mPurchaseId;
    }

    public int getPurchaseState() {
        return mPurchaseState;
    }

    public int getRecurringState() {
        return mRecurringState;
    }

    @Nullable
    public String getDeveloperPayload() {
        return mDeveloperPayload;
    }

    @Nullable
    public String getPurchaseDetails() {
        return mPurchaseDetails;
    }

    @Nullable
    public String getPurchaseSignature() {
        return mPurchaseSignature;
    }

    @Override
    public String toString() {
        try {
            return new JSONObject()
                    .put("purchaseDetails", new JSONObject(mPurchaseDetails))
                    .put("purchaseSignature", mPurchaseSignature)
                    .toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
}
