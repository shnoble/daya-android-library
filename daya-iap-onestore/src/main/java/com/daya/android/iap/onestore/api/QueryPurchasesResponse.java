package com.daya.android.iap.onestore.api;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

class QueryPurchasesResponse {
    @NonNull
    private final List<PurchaseData> mPurchaseDataList = new ArrayList<>();

    @Nullable
    private String mContinuationKey;

    QueryPurchasesResponse(@NonNull Bundle bundle)
            throws SecurityException, NeedUpdateException, IapException {
        int responseCode = bundle.getInt("responseCode");
        IapResult result = IapResult.getResult(responseCode);
        if (result.isFailed()) {
            switch (result) {
                case RESULT_SECURITY_ERROR:
                    throw new SecurityException();
                case RESULT_NEED_UPDATE:
                    throw new NeedUpdateException();
                default:
                    throw new IapException(result);
            }
        }

        ArrayList<String> purchaseDetailsList = bundle.getStringArrayList("purchaseDetailList");
        ArrayList<String> purchaseSignatureList = bundle.getStringArrayList("purchaseSignatureList");
        mContinuationKey = bundle.getString("continuationKey");

        if (purchaseSignatureList != null && purchaseDetailsList != null) {
            for (int i = 0; i < purchaseDetailsList.size(); i++) {
                String purchaseSignature = purchaseSignatureList.get(i);
                String purchaseDetails = purchaseDetailsList.get(i);
                try {
                    mPurchaseDataList.add(new PurchaseData(purchaseDetails, purchaseSignature));
                } catch (JSONException e) {
                    throw new IapException(IapResult.IAP_ERROR_DATA_PARSING);
                }
            }
        }
    }

    @NonNull
    List<PurchaseData> getPurchaseDataList() {
        return mPurchaseDataList;
    }

    @Nullable
    public String getContinuationKey() {
        return mContinuationKey;
    }
}
