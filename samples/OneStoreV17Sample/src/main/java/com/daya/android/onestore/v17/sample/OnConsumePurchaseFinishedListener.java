package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;

interface OnConsumePurchaseFinishedListener {
    void onSuccess(@NonNull String purchaseId);
    void onFailure(int errorCode, @NonNull String errorMessage);
}
