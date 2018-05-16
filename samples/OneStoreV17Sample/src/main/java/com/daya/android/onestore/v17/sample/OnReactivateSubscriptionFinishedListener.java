package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;

interface OnReactivateSubscriptionFinishedListener {
    void onSuccess(@NonNull Purchase purchase);
    void onFailure(int errorCode, @NonNull String errorMessage);
    void onRemoteException();
}
