package com.daya.android.onestore.v17.sample;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

interface QueryProductDetailsFinishedListener {
    void onSuccess(@Nullable List<ProductDetails> productDetailList);
    void onFailure(int errorCode, @NonNull String errorMessage);
    void onRemoteException();
}
