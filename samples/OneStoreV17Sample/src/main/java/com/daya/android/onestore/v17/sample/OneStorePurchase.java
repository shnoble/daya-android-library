package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

class OneStorePurchase {
    private OneStorePurchaseDetails mPurchaseDetails;
    private String mPurchaseSignature;

    OneStorePurchase(@NonNull String purchaseDetails,
                     @NonNull String purchaseSignature) throws JSONException {
        mPurchaseDetails = new OneStorePurchaseDetails(purchaseDetails);
        mPurchaseSignature = purchaseSignature;
    }

    OneStorePurchase() {
    }

    OneStorePurchaseDetails getDetails() {
        return mPurchaseDetails;
    }

    String getSignature() {
        return mPurchaseSignature;
    }

    static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public String toString() {
        try {
            return new JSONObject()
                    .put("purchaseDetails", mPurchaseDetails)
                    .put("purchaseSignature", mPurchaseSignature)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }

    static class Builder {
        private OneStorePurchase mPurchase = new OneStorePurchase();

        private Builder() {
        }

        Builder setPurchaseDetails(@Nullable OneStorePurchaseDetails purchaseDetails) {
            mPurchase.mPurchaseDetails = purchaseDetails;
            return this;
        }

        Builder setPurchaseSignature(@Nullable String purchaseSignature) {
            mPurchase.mPurchaseSignature = purchaseSignature;
            return this;
        }

        OneStorePurchase build() {
            return mPurchase;
        }
    }
}
