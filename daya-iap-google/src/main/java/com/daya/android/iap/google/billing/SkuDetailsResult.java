package com.daya.android.iap.google.billing;

/**
 * Created by shhong on 2018. 2. 22..
 */

import android.support.annotation.NonNull;

import java.util.List;

/** Result list and code for querySkuDetailsAsync method */
class SkuDetailsResult {
    private List<SkuDetails> mSkuDetailsList;
    private IabResult mResult;

    SkuDetailsResult(@NonNull IabResult result, List<SkuDetails> skuDetailsList) {
        this.mSkuDetailsList = skuDetailsList;
        this.mResult = result;
    }

    List<SkuDetails> getSkuDetailsList() {
        return mSkuDetailsList;
    }

    IabResult getResult() {
        return mResult;
    }
}