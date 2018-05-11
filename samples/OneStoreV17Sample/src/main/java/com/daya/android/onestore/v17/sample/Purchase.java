package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

class Purchase {
    private final String mProductId;
    private final PurchaseDetails mPurchaseDetails;
    private final String mPurchaseSignature;

    Purchase(@NonNull String productId, String purchaseDetails, String purchaseSignature) throws JSONException {
        mProductId = productId;
        mPurchaseDetails = new PurchaseDetails(purchaseDetails);
        mPurchaseSignature = purchaseSignature;
    }

    @Override
    public String toString() {
        try {
            return new JSONObject()
                    .put("productId", mProductId)
                    .put("purchaseDetails", mPurchaseDetails.getJSONObject())
                    .put("purchaseSignature", mPurchaseSignature)
                    .toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PurchaseDetails getDetails() {
        return mPurchaseDetails;
    }
}
