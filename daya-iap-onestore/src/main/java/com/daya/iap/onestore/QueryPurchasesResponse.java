package com.daya.iap.onestore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;

import java.util.ArrayList;

class QueryPurchasesResponse {
    @NonNull
    private final IapResult mResult;

    @Nullable
    private ArrayList<String> mPurchaseDetailsList;

    @Nullable
    private ArrayList<String> mPurchaseSignatureList;

    @Nullable
    private String mContinuationKey;

    QueryPurchasesResponse(@NonNull Bundle bundle) {
        int responseCode = bundle.getInt("responseCode");
        mResult = IapResult.getResult(responseCode);
        if (mResult.isSuccess()) {
            mPurchaseDetailsList = bundle.getStringArrayList("purchaseDetailList");
            mPurchaseSignatureList = bundle.getStringArrayList("purchaseSignatureList");
            mContinuationKey = bundle.getString("continuationKey");
        }
    }

    int size() {
        if (mPurchaseDetailsList == null) {
            return 0;
        }
        return mPurchaseDetailsList.size();
    }

    @NonNull
    PurchaseData getPurchaseData(int index) throws JSONException {
        if (mPurchaseSignatureList == null
                || mPurchaseDetailsList == null) {
            throw new NullPointerException();
        }

        String purchaseSignature = mPurchaseSignatureList.get(index);
        String purchaseDetails = mPurchaseDetailsList.get(index);
        return new PurchaseData(purchaseDetails, purchaseSignature);
    }

    @Nullable
    public String getContinuationKey() {
        return mContinuationKey;
    }
}
