package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;

interface OnPurchaseProductFinishedListener {
    void onSuccess(@NonNull String purchaseData, @NonNull String purchaseSignature);
    void onFailure(int errorCode, @NonNull String errorMessage);
}
