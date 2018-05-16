package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

class Purchase {
    private PurchaseDetails mPurchaseDetails;
    private String mPurchaseSignature;

    Purchase(@NonNull String purchaseDetails,
             @NonNull String purchaseSignature) throws JSONException {
        mPurchaseDetails = new PurchaseDetails(purchaseDetails);
        mPurchaseSignature = purchaseSignature;
    }

    Purchase() {
    }

    PurchaseDetails getDetails() {
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
        private Purchase mPurchase = new Purchase();

        private Builder() {
        }

        Builder setPurchaseDetails(@NonNull PurchaseDetails purchaseDetails) {
            mPurchase.mPurchaseDetails = purchaseDetails;
            return this;
        }

        Builder setPurchaseSignature(@NonNull String purchaseSignature) {
            mPurchase.mPurchaseSignature = purchaseSignature;
            return this;
        }

        Purchase build() {
            return mPurchase;
        }
    }
}
