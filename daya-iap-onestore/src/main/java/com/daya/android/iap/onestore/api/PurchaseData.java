package com.daya.android.iap.onestore.api;

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
        mOrderId = jsonObject.getString("orderId");
        mPackageName = jsonObject.getString("packageName");
        mProductId = jsonObject.getString("productId");
        mPurchaseTime = jsonObject.getLong("purchaseTime");
        mPurchaseId = jsonObject.getString("purchaseId");
        mPurchaseState = jsonObject.optInt("purchaseState");
        mRecurringState = jsonObject.optInt("recurringState");
        mDeveloperPayload = jsonObject.optString("developerPayload");
        mPurchaseData = purchaseData;
        mPurchaseSignature = purchaseSignature;
    }

    private PurchaseData() {
    }

    @NonNull
    public String getOrderId() {
        return mOrderId;
    }

    @NonNull
    public String getPackageName() {
        return mPackageName;
    }

    @NonNull
    public String getProductId() {
        return mProductId;
    }

    public long getPurchaseTime() {
        return mPurchaseTime;
    }

    @NonNull
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

    @NonNull
    public String getPurchaseData() {
        return mPurchaseData;
    }

    @NonNull
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
            Validate.notNullOrEmpty(mPurchaseData.mOrderId, "Order ID cannot be null or empty.");
            Validate.notNullOrEmpty(mPurchaseData.mPackageName, "Package name cannot be null or empty.");
            Validate.notNullOrEmpty(mPurchaseData.mProductId, "Product ID cannot be null or empty.");
            if (mPurchaseData.mPurchaseTime <= 0) {
                throw new IllegalStateException("Purchase time can not be less than or equal to zero.");
            }
            Validate.notNullOrEmpty(mPurchaseData.mPurchaseId, "Purchase ID cannot be null or empty.");
            Validate.notNullOrEmpty(mPurchaseData.mPurchaseData, "Purchase data cannot be null or empty.");
            Validate.notNullOrEmpty(mPurchaseData.mPurchaseSignature, "Purchase signature cannot be null or empty.");
            return mPurchaseData;
        }
    }
}
