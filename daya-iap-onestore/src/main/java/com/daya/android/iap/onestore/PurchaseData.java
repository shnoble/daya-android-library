package com.daya.android.iap.onestore;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

public class PurchaseData {
    private String mOrderId;
    private String mPackageName;
    private String mProductId;
    private long mPurchaseTime;
    private String mPurchaseId;
    private int mPurchaseState;
    private int mRecurringState;
    private String mDeveloperPayload;
    private String mPurchaseData;
    private String mPurchaseSignature;

    PurchaseData(@NonNull String purchaseData,
                 @NonNull String purchaseSignature) throws JSONException {
        JSONObject jsonObject = new JSONObject(purchaseData);
        mOrderId = jsonObject.optString("orderId");
        mPackageName = jsonObject.optString("packageName");
        mProductId = jsonObject.optString("productId");
        mPurchaseTime = jsonObject.optLong("purchaseTime");
        mPurchaseId = jsonObject.optString("purchaseId");
        mPurchaseState = jsonObject.optInt("purchaseState");
        mRecurringState = jsonObject.optInt("recurringState");
        mDeveloperPayload = jsonObject.optString("developerPayload");
        mPurchaseData = purchaseData;
        mPurchaseSignature = purchaseSignature;
    }

    private PurchaseData() {
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
    public String getPurchaseData() {
        return mPurchaseData;
    }

    @Nullable
    public String getPurchaseSignature() {
        return mPurchaseSignature;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        try {
            return new JSONObject()
                    .put("purchaseData", new JSONObject(mPurchaseData))
                    .put("purchaseSignature", mPurchaseSignature)
                    .toString(2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }

    public static class Builder {
        private final PurchaseData mPurchaseData = new PurchaseData();

        @NonNull
        public Builder setOrderId(@Nullable String orderId) {
            mPurchaseData.mOrderId = orderId;
            return this;
        }

        @NonNull
        public Builder setPackageName(@Nullable String packageName) {
            mPurchaseData.mPackageName = packageName;
            return this;
        }

        @NonNull
        public Builder setProductId(@Nullable String productId) {
            mPurchaseData.mProductId = productId;
            return this;
        }

        @NonNull
        public Builder setPurchaseTime(long purchaseTime) {
            mPurchaseData.mPurchaseTime = purchaseTime;
            return this;
        }

        @NonNull
        public Builder setPurchaseId(@Nullable String purchaseId) {
            mPurchaseData.mPurchaseId = purchaseId;
            return this;
        }

        @NonNull
        public Builder setPurchaseState(int purchaseState) {
            mPurchaseData.mPurchaseState = purchaseState;
            return this;
        }

        @NonNull
        public Builder setRecurringState(int recurringState) {
            mPurchaseData.mRecurringState = recurringState;
            return this;
        }

        @NonNull
        public Builder setDeveloperPayload(@Nullable String developerPayload) {
            mPurchaseData.mDeveloperPayload = developerPayload;
            return this;
        }

        @NonNull
        public Builder setPurchaseData(@Nullable String purchaseData) {
            mPurchaseData.mPurchaseData = purchaseData;
            return this;
        }

        @NonNull
        public Builder setPurchaseSignature(@Nullable String purchaseSignature) {
            mPurchaseData.mPurchaseSignature = purchaseSignature;
            return this;
        }

        @NonNull
        public PurchaseData build() {
            return mPurchaseData;
        }
    }
}
