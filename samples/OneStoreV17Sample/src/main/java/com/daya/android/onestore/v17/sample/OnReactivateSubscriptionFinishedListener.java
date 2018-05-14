package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

interface OnReactivateSubscriptionFinishedListener {
    void onSuccess(@Nullable String purchaseId);
    void onFailure(int errorCode, @NonNull String errorMessage);
}
