package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;

import java.util.List;

interface QueryPurchasesFinishedListener {
    void onSuccess(@NonNull List<Purchase> purchases);
    void onFailure(int errorCode, @NonNull String errorMessage);
    void onRemoteException();
}
