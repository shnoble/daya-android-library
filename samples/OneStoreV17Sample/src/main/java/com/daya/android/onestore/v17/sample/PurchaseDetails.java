package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

class PurchaseDetails {
    private static final String ORDER_ID_KEY = "orderId";
    private static final String PRODUCT_ID_KEY = "productId";
    private static final String PURCHASE_ID_KEY = "purchaseId";
    private static final String PURCHASE_TIME_KEY = "purchaseTime";
    private static final String PURCHASE_STATE_KEY = "purchaseState";
    private static final String RECURRING_STATE_KEY = "recurringState";
    private static final String PACKAGE_NAME_KEY = "packageName";
    private static final String DEVELOPER_PAYLOAD_KEY = "developerPayload";

    private String mOrderId;
    private String mProductId;
    private String mPurchaseId;
    private long mPurchaseTime;
    private int mPurchaseState;
    private int mRecurringState;
    private String mPackageName;
    private String mDeveloperPayload;
    private String mOrginPurchaseData;

    PurchaseDetails(@NonNull String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        mOrderId = jsonObject.optString(ORDER_ID_KEY);
        mProductId = jsonObject.optString(PRODUCT_ID_KEY);
        mPurchaseId = jsonObject.optString(PURCHASE_ID_KEY);
        mPurchaseTime = jsonObject.optLong(PURCHASE_TIME_KEY);
        mPurchaseState = jsonObject.optInt(PURCHASE_STATE_KEY, 0);
        mRecurringState = jsonObject.optInt(RECURRING_STATE_KEY, 0);
        mPackageName = jsonObject.optString(PACKAGE_NAME_KEY);
        mDeveloperPayload = jsonObject.optString(DEVELOPER_PAYLOAD_KEY);
        mOrginPurchaseData = jsonString;
    }

    private PurchaseDetails() {
    }

    public String getOrderId() {
        return mOrderId;
    }

    public String getProductId() {
        return mProductId;
    }

    public String getPurchaseId() {
        return mPurchaseId;
    }

    public long getPurchaseTime() {
        return mPurchaseTime;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public int getPurchaseState() {
        return mPurchaseState;
    }

    public int getRecurringState() {
        return mRecurringState;
    }

    public String getDeveloperPayload() {
        return mDeveloperPayload;
    }

    public String getOrginPurchaseData() {
        return mOrginPurchaseData;
    }

    @Override
    public String toString() {
        try {
            return new JSONObject()
                    .put(ORDER_ID_KEY, mOrderId)
                    .put(PRODUCT_ID_KEY, mProductId)
                    .put(PURCHASE_ID_KEY, mPurchaseId)
                    .put(PURCHASE_TIME_KEY, mPurchaseTime)
                    .put(PURCHASE_STATE_KEY, mPurchaseState)
                    .put(RECURRING_STATE_KEY, mRecurringState)
                    .put(PACKAGE_NAME_KEY, mPackageName)
                    .put(DEVELOPER_PAYLOAD_KEY, mDeveloperPayload)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }

    static Builder newBuilder() {
        return new Builder();
    }

    static class Builder {
        private PurchaseDetails mPurchaseDetails = new PurchaseDetails();

        private Builder() {
        }

        Builder setOrderId(@NonNull String orderId) {
            mPurchaseDetails.mOrderId = orderId;
            return this;
        }

        Builder setProductId(@NonNull String productId) {
            mPurchaseDetails.mProductId = productId;
            return this;
        }

        Builder setPurchaseId(@NonNull String purchaseId) {
            mPurchaseDetails.mPurchaseId = purchaseId;
            return this;
        }

        Builder setPurchaseTime(long purchaseTime) {
            mPurchaseDetails.mPurchaseTime = purchaseTime;
            return this;
        }

        Builder setPurchaseState(int purchaseState) {
            mPurchaseDetails.mPurchaseState = purchaseState;
            return this;
        }

        Builder setRecurringState(int recurringState) {
            mPurchaseDetails.mRecurringState = recurringState;
            return this;
        }

        Builder setPackageName(@NonNull String packageName) {
            mPurchaseDetails.mPackageName = packageName;
            return this;
        }

        Builder setDeveloperPayload(@NonNull String developerPayload) {
            mPurchaseDetails.mDeveloperPayload = developerPayload;
            return this;
        }

        Builder setOriginPurchaseData(@NonNull String originPurchaseData) {
            mPurchaseDetails.mOrginPurchaseData = originPurchaseData;
            return this;
        }

        PurchaseDetails build() {
            return mPurchaseDetails;
        }
    }
}
