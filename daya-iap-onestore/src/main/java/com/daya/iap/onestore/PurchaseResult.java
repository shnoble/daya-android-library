package com.daya.iap.onestore;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

class PurchaseResult {
    @Nullable
    private final String mPurchaseData;

    @Nullable
    private final String mPurchaseSignature;

    PurchaseResult(@NonNull Intent data)
            throws SecurityException, NeedUpdateException, IapException {
        int responseCode = data.getIntExtra("responseCode", -1);
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

        mPurchaseData = data.getStringExtra("purchaseData");
        mPurchaseSignature = data.getStringExtra("purchaseSignature");
    }

    @Nullable
    String getPurchaseData() {
        return mPurchaseData;
    }

    @Nullable
    String getPurchaseSignature() {
        return mPurchaseSignature;
    }
}
